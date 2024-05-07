package com.example.ticketservice.batch;

import com.example.ticketservice.entity.CenterMembership;
import com.example.ticketservice.entity.CenterMembershipStatusEnum;
import com.example.ticketservice.entity.UserTicket;
import com.example.ticketservice.repository.centermembership.CenterMembershipRepository;
import com.example.ticketservice.repository.ticket.UserTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class ExpiringSoonCenterMembershipBatchConfig {
    private final UserTicketRepository userTicketRepository;
    private final CenterMembershipRepository centerMembershipRepository;

    @Bean
    public Job expiringSoonCenterMembershipJob(JobRepository jobRepository, Step expiringSoonCenterMembershipStep) {
        return new JobBuilder("expiringSoonCenterMembershipJob", jobRepository)
                .start(expiringSoonCenterMembershipStep)
                .build();
    }

//    @Bean
//    public Step expiringSoonCenterMembershipStep(
//            JobRepository jobRepository,
//            PlatformTransactionManager transactionManager
//    ) {
//        return new StepBuilder("expiringSoonCenterMembershipStep", jobRepository)
//                .<UserTicket, UserTicket>chunk(100, transactionManager)
//                .reader(expiringSoonUserTicketReader())
//                .writer(expiringSoonCenterMembershipWriter())
//                .build();
//    }

    @Bean
    public RepositoryItemReader<UserTicket> expiringSoonUserTicketReader() {
        LocalDate thirtyDaysFromNow = LocalDate.now().plusDays(30);

        return new RepositoryItemReaderBuilder<UserTicket>()
                .name("expiringSoonUserTicketReader")
                .repository(userTicketRepository)
                .methodName("findByEndDateBefore")
                .arguments(Collections.singletonList(thirtyDaysFromNow))
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .pageSize(100)
                .build();
    }

    @Bean
    public ItemWriter<UserTicket> expiringSoonCenterMembershipWriter() {
        return items -> {
            Set<Long> processedMembers = new HashSet<>();

            for (UserTicket ticket : items) {
                Long memberId = ticket.getMemberId();
                if (!processedMembers.contains(memberId)) {
                    processedMembers.add(memberId);
                    CenterMembership membership = centerMembershipRepository.findByMemberIdAndCenterId(memberId, ticket.getTicket().getCenterId());
                    if (membership != null && membership.getStatus() != CenterMembershipStatusEnum.EXPIRED.getStatusType()) {
                        membership.setStatus(CenterMembershipStatusEnum.EXPIRING_SOON.getStatusType());
                        centerMembershipRepository.save(membership);
                    }
                }
            }
        };
    }
}
