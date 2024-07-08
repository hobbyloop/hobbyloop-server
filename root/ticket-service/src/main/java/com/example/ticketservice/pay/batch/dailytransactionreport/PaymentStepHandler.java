package com.example.ticketservice.pay.batch.dailytransactionreport;

import com.example.ticketservice.pay.batch.dailytransactionreport.dto.PaymentStepMetrics;
import com.example.ticketservice.pay.entity.member.DailyTransactionReport;
import com.example.ticketservice.pay.entity.member.Payment;
import com.example.ticketservice.pay.entity.member.enums.PaymentStatusEnum;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PaymentStepHandler {
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    @StepScope
    public JpaPagingItemReader<Payment> paymentReader() {
        LocalDateTime startDate = LocalDate.now().minusDays(1).atStartOfDay();
        LocalDateTime endDate = LocalDate.now().minusDays(1).atTime(23, 59, 59);

        return new JpaPagingItemReaderBuilder<Payment>()
                .name("paymentReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT p FROM Payment p WHERE p.createdAt BETWEEN :startDate AND :endDate")
                .parameterValues(Map.of("startDate", startDate, "endDate", endDate))
                .pageSize(100)
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<Payment, PaymentStepMetrics> paymentProcessor() {
        return payment -> {
            Integer totalTransactionCount = 1;
            Long netSales = 0L;
            Integer paymentFailedCount = 0;
            Integer paymentAttemptedCount = 1;
            Integer paymentSuccessCount = 0;
            Integer transactionHour;
            Long memberId;

            if (payment.getStatus() == PaymentStatusEnum.SUCCESS.getValue()) {
                netSales = payment.getAmount();
                paymentSuccessCount = 1;
            }
            if (payment.getStatus() == PaymentStatusEnum.FAILURE.getValue()) {
                paymentFailedCount = 1;
            }
            transactionHour = payment.getCreatedAt().getHour();
            memberId = payment.getMemberId();

            return new PaymentStepMetrics(
                    totalTransactionCount,
                    netSales,
                    paymentFailedCount,
                    paymentAttemptedCount,
                    paymentSuccessCount,
                    transactionHour,
                    memberId
            );
        };
    }

    @Bean
    @StepScope
    public ItemWriter<PaymentStepMetrics> paymentWriter() {
        return chunk -> {
            StepExecution stepExecution = StepSynchronizationManager.getContext().getStepExecution();

            // 메트릭 계산
            Integer totalTransactionCount = chunk.getItems().size();
            Long netSales = chunk.getItems().stream()
                    .mapToLong(PaymentStepMetrics::getNetSales)
                    .sum();
            Integer paymentFailedCount = chunk.getItems().stream()
                    .mapToInt(PaymentStepMetrics::getPaymentFailedCount)
                    .sum();
            Integer paymentAttemptedCount = chunk.getItems().size();
            Integer paymentSuccessCount = chunk.getItems().stream()
                    .mapToInt(PaymentStepMetrics::getPaymentSuccessCount)
                    .sum();

            Map<Integer, Integer> transactionCountByHour = chunk.getItems().stream()
                    .collect(Collectors.groupingBy(
                            PaymentStepMetrics::getTransactionHour,
                            Collectors.summingInt(PaymentStepMetrics::getTotalTransactionCount)
                    ));

            // 가장 거래가 많이 일어난 시간대 찾기
            Integer peakTransactionHour = transactionCountByHour.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(null);

            Long totalCustomerCount = chunk.getItems().stream()
                    .map(PaymentStepMetrics::getMemberId)
                    .distinct()
                    .count();

            ExecutionContext stepContext = stepExecution.getExecutionContext();

            DailyTransactionReport report = (DailyTransactionReport) stepContext.get("dailyTransactionReport");

            report.setTotalTransactionCount(totalTransactionCount);
            report.setNetSales(netSales);
            report.setPaymentFailedCount(paymentFailedCount);
            report.setPaymentAttemptCount(paymentAttemptedCount);
            report.setPaymentSuccessCount(paymentSuccessCount);
            report.setTotalCustomerCount(totalCustomerCount.intValue());
            report.setPeakTransactionHour(peakTransactionHour);

            stepContext.put("dailyTransactionReport", report);
        };
    }
}
