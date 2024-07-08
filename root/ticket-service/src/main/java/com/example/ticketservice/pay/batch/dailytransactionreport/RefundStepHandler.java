package com.example.ticketservice.pay.batch.dailytransactionreport;

import com.example.ticketservice.pay.batch.dailytransactionreport.dto.RefundStepMetrics;
import com.example.ticketservice.pay.entity.member.DailyTransactionReport;
import com.example.ticketservice.pay.entity.member.Payment;
import com.example.ticketservice.pay.entity.member.PaymentRefund;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RefundStepHandler {
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    @StepScope
    public JpaPagingItemReader<PaymentRefund> refundReader() {
        LocalDateTime startDate = LocalDate.now().minusDays(1).atStartOfDay();
        LocalDateTime endDate = LocalDate.now().minusDays(1).atTime(23, 59, 59);

        return new JpaPagingItemReaderBuilder<PaymentRefund>()
                .name("refundReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT p FROM PaymentRefund p WHERE p.createdAt BETWEEN :startDate AND :endDate")
                .parameterValues(Map.of("startDate", startDate, "endDate", endDate))
                .pageSize(100)
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<PaymentRefund, RefundStepMetrics> refundProcessor() {
        return refund -> {
            Long refundAmount = 0L;
            Integer refundCount = 0;
            if (refund.getStatus() == PaymentStatusEnum.SUCCESS.getValue()) {
                refundAmount = refund.getAmount();
                refundCount = 1;
            }

            return new RefundStepMetrics(refundAmount, refundCount);
        };
    }

    @Bean
    @StepScope
    public ItemWriter<RefundStepMetrics> refundWriter() {
        return chunk -> {
            StepExecution stepExecution = StepSynchronizationManager.getContext().getStepExecution();

            Long refundAmount = chunk.getItems().stream()
                    .mapToLong(RefundStepMetrics::getRefundAmount)
                    .sum();

            Integer refundCount = chunk.getItems().stream()
                    .mapToInt(RefundStepMetrics::getRefundCount)
                    .sum();

            ExecutionContext stepContext = stepExecution.getExecutionContext();

            DailyTransactionReport report = (DailyTransactionReport) stepContext.get("dailyTransactionReport");

            report.setRefundAmount(refundAmount);
            report.setTotalRefundCount(refundCount);

            stepContext.put("dailyTransactionReport", report);
        };
    }
}
