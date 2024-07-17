package com.example.ticketservice.pay.batch.dailytransactionreport;

import com.example.ticketservice.pay.batch.dailytransactionreport.dto.CheckoutStepMetrics;
import com.example.ticketservice.pay.entity.member.Checkout;
import com.example.ticketservice.pay.entity.member.DailyTransactionReport;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
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
public class CheckoutStepHandler {
    private final EntityManagerFactory entityManagerFactory;

//    @Value("#{jobParameters['startDate']}") LocalDateTime startDate,
//    @Value("#{jobParameters['endDate']}") LocalDateTime endDate

    @Bean
    @StepScope
    public JpaPagingItemReader<Checkout> checkoutReader() {
        LocalDateTime startDate = LocalDate.now().minusDays(1).atStartOfDay();
        LocalDateTime endDate = LocalDate.now().minusDays(1).atTime(23, 59, 59);

        return new JpaPagingItemReaderBuilder<Checkout>()
                .name("checkoutReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT c FROM Checkout c WHERE c.createdAt BETWEEN :startDate AND :endDate")
                .parameterValues(Map.of("startDate", startDate, "endDate", endDate))
                .pageSize(100)
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<Checkout, CheckoutStepMetrics> checkoutProcessor() {
        return checkout -> {
            return new CheckoutStepMetrics(1, checkout.getTotalDiscountAmount());
        };
    }

    @Bean
    @StepScope
    public ItemWriter<CheckoutStepMetrics> checkoutWriter() {
        return chunk -> {
            StepExecution stepExecution = StepSynchronizationManager.getContext().getStepExecution();

            Integer checkoutCount = chunk.getItems().stream()
                    .mapToInt(CheckoutStepMetrics::getCheckoutCount)
                    .sum();

            Long totalDiscountAmount = chunk.getItems().stream()
                    .mapToLong(CheckoutStepMetrics::getTotalDiscountAmount)
                    .sum();

            ExecutionContext stepContext = stepExecution.getExecutionContext();

            DailyTransactionReport report = new DailyTransactionReport();
            report.setCheckoutCount(checkoutCount);
            report.setTotalDiscountAmount(totalDiscountAmount);

            stepContext.put("dailyTransactionReport", report);
        };
    }

}
