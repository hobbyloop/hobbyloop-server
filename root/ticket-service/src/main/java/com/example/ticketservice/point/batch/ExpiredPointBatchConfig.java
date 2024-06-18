package com.example.ticketservice.point.batch;

import com.example.ticketservice.point.entity.Point;
import com.example.ticketservice.point.entity.PointHistory;
import com.example.ticketservice.point.entity.enums.PointTypeEnum;
import com.example.ticketservice.point.entity.enums.PointUsableScopeEnum;
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
public class ExpiredPointBatchConfig {
    private final EntityManagerFactory entityManagerFactory;
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
                .<PointHistory, PointHistory>chunk(100, transactionManager)
                .reader(expiredPointHistoryReader())
                .processor(expiredPointProcessor())
                .writer(expiredPointHistoryWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<PointHistory> expiredPointHistoryReader() {
        // PointHistory의 type이 EARN이고, expirationStartDate가 된 데이터만 조회
        return new JpaPagingItemReaderBuilder<PointHistory>()
                .name("expiredPointHistoryReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(100)
                .queryString("SELECT ph FROM PointHistory ph WHERE ph.type = :type AND ph.expirationDateTime <= :now AND ph.isProcessedByBatch = false")
                .parameterValues(Map.of("type", PointTypeEnum.EARN.getValue(), "now", LocalDateTime.now()))
                .build();
    }

    @Bean
    public ItemProcessor<PointHistory, PointHistory> expiredPointProcessor() {
        return pointHistory -> {
            Point point;
            if (pointHistory.isGeneralPoint()) {
                point = pointRepository.findByMemberIdAndUsableScopeIs(pointHistory.getMemberId(), PointUsableScopeEnum.GENERAL.getValue()).orElseThrow();
                Long expiredAmount = pointHistory.getAmount();
                point.expire(expiredAmount);

                // 포인트 소멸 내역 저장
                PointHistory expiredHistory = PointHistory.builder()
                        .memberId(pointHistory.getMemberId())
                        .type(PointTypeEnum.EXPIRE.getValue())
                        .amount(expiredAmount)
                        .balance(point.getBalance())
                        .description(pointHistory.getDescription() +" 소멸")
                        .build();

                pointHistory.processByBatch();

                pointHistoryRepository.save(pointHistory);
                pointRepository.save(point);

                return expiredHistory;
            } else if (pointHistory.isCompanyPoint()) {
                point = pointRepository.findByMemberIdAndCompanyId(
                        pointHistory.getMemberId(), pointHistory.getCompanyId()
                ).orElseThrow();

                Long expiredAmount = pointHistory.getAmount();
                point.expire(expiredAmount);

                // 포인트 소멸 내역 저장
                PointHistory expiredHistory = PointHistory.builder()
                        .memberId(pointHistory.getMemberId())
                        .type(PointTypeEnum.EXPIRE.getValue())
                        .companyId(pointHistory.getCompanyId())
                        .amount(expiredAmount)
                        .balance(point.getBalance())
                        .description(pointHistory.getDescription() +" 소멸")
                        .build();

                pointHistory.processByBatch();

                pointHistoryRepository.save(pointHistory);
                pointRepository.save(point);

                return expiredHistory;
            } else if (pointHistory.isCenterPoint()) {
                point = pointRepository.findByMemberIdAndCenterId(
                        pointHistory.getMemberId(), pointHistory.getCenterId()
                ).orElseThrow();

                Long expiredAmount = pointHistory.getAmount();
                point.expire(expiredAmount);

                // 포인트 소멸 내역 저장
                PointHistory expiredHistory = PointHistory.builder()
                        .memberId(pointHistory.getMemberId())
                        .type(PointTypeEnum.EXPIRE.getValue())
                        .centerId(pointHistory.getCenterId())
                        .amount(expiredAmount)
                        .balance(point.getBalance())
                        .description(pointHistory.getDescription() +" 소멸")
                        .build();

                pointHistory.processByBatch();

                pointHistoryRepository.save(pointHistory);
                pointRepository.save(point);

                return expiredHistory;
            }

            return null;
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
