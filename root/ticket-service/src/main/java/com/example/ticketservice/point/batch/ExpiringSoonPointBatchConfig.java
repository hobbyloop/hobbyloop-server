package com.example.ticketservice.point.batch;

import com.example.ticketservice.point.entity.Point;
import com.example.ticketservice.point.entity.PointHistory;
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
    private final PointRepository pointRepository;

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
                .<Point, Point>chunk(100, transactionManager)
                .reader(expiringSoonPointReader())
                .processor(expiringSoonPointProcessor())
                .writer(expiringSoonPointWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<Point> expiringSoonPointReader() {
        return new JpaPagingItemReaderBuilder<Point>()
                .name("expiringSoonPointReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(100)
                .queryString("SELECT p FROM Point p WHERE p.expirationDateTime BETWEEN :now AND :thirtyDaysLater")
                .parameterValues(Map.of("now", LocalDateTime.now(),
                                        "thirtyDaysLater", LocalDateTime.now().plusDays(30)))
                .build();
    }

    @Bean
    public ItemProcessor<Point, Point> expiringSoonPointProcessor() {
        return point -> {
            point.markExpiredSoon();
            point.setUpdatedBy("batch");
            return point;
        };
    }

    @Bean
    public ItemWriter<Point> expiringSoonPointWriter() {
        return items -> {
            for (Point point : items) {
                pointRepository.save(point);
            }
        };
    }
}
