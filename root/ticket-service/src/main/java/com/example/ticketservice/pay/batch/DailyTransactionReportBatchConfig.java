package com.example.ticketservice.pay.batch;

import com.example.ticketservice.pay.repository.DailyTransactionReportRepository;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class DailyTransactionReportBatchConfig {
    private final EntityManagerFactory entityManagerFactory;
    private final DailyTransactionReportRepository dailyTransactionReportRepository;

    @Bean
    public Job dailyTransactionReportJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("dailyTransactionReportJob", jobRepository)
                .start(checkoutStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step checkoutStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager
    ) {
        return new StepBuilder("checkoutStep", jobRepository)
                .<>chunk(100, transactionManager)
                .reader()
                .processor()
                .writer()
                .build();

    }
}
