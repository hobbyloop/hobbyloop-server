package com.example.ticketservice.pay.batch.dailytransactionreport;

import com.example.ticketservice.pay.entity.member.Checkout;
import com.example.ticketservice.pay.entity.member.DailyTransactionReport;
import com.example.ticketservice.pay.repository.DailyTransactionReportRepository;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CheckoutStepHandler {
    private final EntityManagerFactory entityManagerFactory;
    private final DailyTransactionReportRepository transactionReportRepository;

    @Bean
    @StepScope
    public JpaPagingItemReader<Checkout> checkoutReader(@Value("#{jobParameters['startDate']}") LocalDateTime startDate,
                                                        @Value("#{jobParameters['endDate']}") LocalDateTime endDate) {
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
    public ItemProcessor<List<Checkout>, DailyTransactionReport> checkoutProcessor() {
        return checkouts -> {
            int checkoutCount = checkouts.size();
            Long totalDiscountAmount = checkouts.stream()
                    .mapToLong(Checkout::getTotalDiscountAmount)
                    .sum();

            return DailyTransactionReport.builder()
                    .checkoutCount(checkoutCount)
                    .totalDiscountAmount(totalDiscountAmount)
                    .build();
        };
    }

    @Bean
    public CompositeItemProcessor<Checkout, DailyTransactionReport> compositeProcessor(ItemProcessor<List<Checkout>, DailyTransactionReport> checkoutProcessor) {
        List<ItemProcessor<?, ?>> processors = new ArrayList<>();
        processors.add(new ItemProcessor<Checkout, Checkout>() {
            @Override
            public Checkout process(Checkout item) throws Exception {
                return item;
            }
        });
        processors.add(new ItemProcessor<Checkout, List<Checkout>>() {
            private List<Checkout> items = new ArrayList<>();

            @Override
            public List<Checkout> process(Checkout item) throws Exception {
                items.add(item);
                return items;
            }
        });
        processors.add(checkoutProcessor);

        CompositeItemProcessor<Checkout, DailyTransactionReport> processor = new CompositeItemProcessor<>();
        processor.setDelegates(processors);
        return processor;
    }

    @Bean
    public JpaItemWriter<DailyTransactionReport> dailyTransactionReportWriter() {
        JpaItemWriter<DailyTransactionReport> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }
}
