package com.example.ticketservice.point.batch;

import com.example.ticketservice.point.entity.PointHistory;
import com.example.ticketservice.point.entity.enums.PointTypeEnum;
import com.example.ticketservice.point.repository.PointHistoryRepository;
import com.example.ticketservice.point.repository.PointRepository;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ExpiringSoonPointBatchConfig {
    private final EntityManagerFactory entityManagerFactory;
    private final PointHistoryRepository pointHistoryRepository;

    @Bean
    public Job expiringSoonPointJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("expiringSoonPointJob", jobRepository)
                .start(expiringSoonPointStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step expiringSoonPointStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager
    ) {
        return new StepBuilder("expiringSoonPointStep", jobRepository)
                .<PointHistory, PointHistory>chunk(100, transactionManager)
                .reader(expiringSoonPointHistoryReader())
                .processor(expiringSoonPointHistoryProcessor())
                .writer(expiringSoonPointHistoryWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<PointHistory> expiringSoonPointHistoryReader() {
        return new JpaPagingItemReaderBuilder<PointHistory>()
                .name("expiringSoonPointHistoryReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(100)
                .queryString("SELECT ph FROM PointHistory ph WHERE ph.type = :type AND ph.expirationDateTime BETWEEN :now AND :thirtyDaysLater AND ph.isProcessedByBatch = false")
                .parameterValues(Map.of("type", PointTypeEnum.EARN.getValue(),
                                        "now", LocalDateTime.now(),
                                        "thirtyDaysLater", LocalDateTime.now().plusDays(30)))
                .build();
    }

    @Bean
    public ItemProcessor<PointHistory, PointHistory> expiringSoonPointHistoryProcessor() {
        return pointHistory -> {
            pointHistory.markExpiredSoon();
            return pointHistory;
        };
    }

    @Bean
    public ItemWriter<PointHistory> expiringSoonPointHistoryWriter() {
        return items -> {
            for (PointHistory history : items) {
                pointHistoryRepository.save(history);
            }
        };
    }
}
