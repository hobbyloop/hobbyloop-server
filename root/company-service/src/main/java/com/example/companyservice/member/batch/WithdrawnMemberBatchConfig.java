package com.example.companyservice.member.batch;

import com.example.companyservice.member.entity.Member;
import com.example.companyservice.member.entity.MemberArchive;
import com.example.companyservice.member.repository.MemberArchiveRepository;
import com.example.companyservice.member.repository.MemberRepository;
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
import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class WithdrawnMemberBatchConfig {
    private final EntityManagerFactory entityManagerFactory;

    private final MemberArchiveRepository memberArchiveRepository;

    private final MemberRepository memberRepository;

    @Bean
    public Job withdrawnMemberJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("withdrawnMemberJob", jobRepository)
                .start(withdrawnMemberStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step withdrawnMemberStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager
    ) {
        return new StepBuilder("withdrawnMemberStep", jobRepository)
                .<Member, Member>chunk(100, transactionManager)
                .reader(withdrawnMemberReader())
                .processor(withdrawnMemberProcessor())
                .writer(withdrawnMemberWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<Member> withdrawnMemberReader() {
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusDays(90);

        return new JpaPagingItemReaderBuilder<Member>()
                .name("withdrawnMemberReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(100)
                .queryString("SELECT m FROM Member m WHERE m.isDeleted = true AND deletedAt < :threeMonthsAgo")
                .parameterValues(Collections.singletonMap("threeMonthsAgo", threeMonthsAgo))
                .build();
    }

    @Bean
    public ItemProcessor<Member, Member> withdrawnMemberProcessor() {
        return member -> {
            MemberArchive archive = MemberArchive.from(member);
            archive.setCreatedBy("batch");
            memberArchiveRepository.save(archive);

            return member;
        };
    }

    @Bean
    public ItemWriter<Member> withdrawnMemberWriter() {
        return members -> {
            memberRepository.deleteAll(members);
        };
    }
}
