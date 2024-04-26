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
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final UserTicketRepository userTicketRepository;
    private final CenterMembershipRepository centerMembershipRepository;

    @Bean
    public Job expiredCenterMembershipJob(JobRepository jobRepository, Step expiredCenterMembershipStep) {
        return new JobBuilder("expiredCenterMembership", jobRepository)
                .start(expiredCenterMembershipStep)
                .build();
    }

    @Bean
    public Step expiredCenterMembershipStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager
    ) {
        return new StepBuilder("expiredCenterMembershipStep", jobRepository)
                .<UserTicket, Map<String, List<UserTicket>>>chunk(100, transactionManager)
                .reader(userTicketReader())
                .processor(groupByMemberAndCenterProcessor())
                .writer(expiredCenterMembershipWriter())
                .build();
    }

    @Bean
    public RepositoryItemReader<UserTicket> userTicketReader() {
        return new RepositoryItemReaderBuilder<UserTicket>()
                .name("userTicketReader")
                .repository(userTicketRepository)
                .methodName("findAll")
                .build();
    }

    @Bean
    public ItemProcessor<UserTicket, Map<String, List<UserTicket>>> groupByMemberAndCenterProcessor() {
        return new ItemProcessor<UserTicket, Map<String, List<UserTicket>>>() {
            private Map<String, List<UserTicket>> ticketsByMemberAndCenter = new HashMap<>();

            @Override
            public Map<String, List<UserTicket>> process(UserTicket item) throws Exception {
                String key = item.getMemberId() + "_" + item.getTicket().getCenterId();
                List<UserTicket> tickets = ticketsByMemberAndCenter.getOrDefault(key, new ArrayList<>());
                tickets.add(item);
                ticketsByMemberAndCenter.put(key, tickets);
                return getResult();
            }


            public Map<String, List<UserTicket>> getResult() {
                Map<String, List<UserTicket>> expiredMemberships = new HashMap<>();
                for (Map.Entry<String, List<UserTicket>> entry : ticketsByMemberAndCenter.entrySet()) {
                    if (entry.getValue().stream().noneMatch(ticket ->
                            ticket.getEndDate().isAfter(LocalDate.now()) && ticket.getRemainingCount() > 0)) {
                        expiredMemberships.put(entry.getKey(), entry.getValue());
                    }
                }

                return expiredMemberships;
            }
        };
    }

    @Bean
    public ItemWriter<Map<String, List<UserTicket>>> expiredCenterMembershipWriter() {
        return items -> {
            for (Map<String, List<UserTicket>> expiredMemberships : items) {
                for (Map.Entry<String, List<UserTicket>> entry : expiredMemberships.entrySet()) {
                    String[] memberAndCenter = entry.getKey().split("_");
                    Long memberId = Long.valueOf(memberAndCenter[0]);
                    Long centerId = Long.valueOf(memberAndCenter[1]);

                    CenterMembership membership = centerMembershipRepository.findByMemberIdAndCenterId(memberId, centerId);
                    if (membership != null) {
                        membership.setStatus(CenterMembershipStatusEnum.EXPIRED.getStatusType());
                        centerMembershipRepository.save(membership);
                    }
                }
            }
        };
    }
}
