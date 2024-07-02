package com.example.ticketservice.point.batch;

import com.example.ticketservice.point.entity.Point;
import com.example.ticketservice.point.entity.Points;
import com.example.ticketservice.point.entity.PointHistory;
import com.example.ticketservice.point.entity.enums.PointTypeEnum;
import com.example.ticketservice.point.entity.enums.PointUsableScopeEnum;
import com.example.ticketservice.point.repository.PointHistoryRepository;
import com.example.ticketservice.point.repository.PointRepository;
import com.example.ticketservice.point.repository.PointsRepository;
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
public class ExpiredPointBatchConfig {
    private final EntityManagerFactory entityManagerFactory;
    private final PointsRepository pointsRepository;
    private final PointRepository pointRepository;
    private final PointHistoryRepository pointHistoryRepository;

    @Bean
    public Job expiredPointJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("expiredPointJob", jobRepository)
                .start(expiredPointStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step expiredPointStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager
    ) {
        return new StepBuilder("expiredPointStep", jobRepository)
                .<Point, PointHistory>chunk(100, transactionManager)
                .reader(expiredPointReader())
                .processor(expiredPointProcessor())
                .writer(expiredPointHistoryWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<Point> expiredPointReader() {
        return new JpaPagingItemReaderBuilder<Point>()
                .name("expiredPointReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(100)
                .queryString("SELECT p FROM Point p WHERE p.expirationDateTime <= :now")
                .parameterValues(Map.of("now", LocalDateTime.now()))
                .build();
    }

    @Bean
    public ItemProcessor<Point, PointHistory> expiredPointProcessor() {
        return point -> {
            Points points = point.getPoints();

            points.expire(point.getAmount());
            pointsRepository.save(points);

            pointRepository.delete(point);

            PointHistory expiredHistory = PointHistory.builder()
                    .memberId(points.getMemberId())
                    .type(PointTypeEnum.EXPIRE.getValue())
                    .amount(point.getAmount())
                    .balance(points.getBalance())
                    .description("소멸")
                    .build();

            return expiredHistory;
        };
    }

    @Bean
    public ItemWriter<PointHistory> expiredPointHistoryWriter() {
        return items -> {
            for (PointHistory history : items) {
                pointHistoryRepository.save(history);
            }
        };
    }

}
