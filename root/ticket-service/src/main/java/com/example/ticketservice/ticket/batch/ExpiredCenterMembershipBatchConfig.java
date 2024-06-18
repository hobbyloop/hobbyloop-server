package com.example.ticketservice.ticket.batch;

import com.example.ticketservice.ticket.entity.CenterMembership;
import com.example.ticketservice.ticket.entity.CenterMembershipStatusEnum;
import com.example.ticketservice.ticket.entity.UserTicket;
import com.example.ticketservice.ticket.repository.centermembership.CenterMembershipRepository;
import com.example.ticketservice.ticket.repository.ticket.UserTicketRepository;
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
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ExpiredCenterMembershipBatchConfig {
    private final UserTicketRepository userTicketRepository;
    private final CenterMembershipRepository centerMembershipRepository;

    @Bean
    public Job expiredCenterMembershipJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("expiredCenterMembershipJob", jobRepository)
                .start(expiredCenterMembershipStep(jobRepository, transactionManager))
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
        Map<String, Sort.Direction> sorts = new HashMap<>();
        sorts.put("id", Sort.Direction.ASC);

        return new RepositoryItemReaderBuilder<UserTicket>()
                .name("userTicketReader")
                .repository(userTicketRepository)
                .methodName("findAll")
                .sorts(sorts)
                .pageSize(100)
                .build();
    }

    @Bean
    public ItemProcessor<UserTicket, Map<String, List<UserTicket>>> groupByMemberAndCenterProcessor() {
        return new ItemProcessor<UserTicket, Map<String, List<UserTicket>>>() {
            private Map<String, List<UserTicket>> ticketsByMemberAndCenter = new HashMap<>();

            @Override
            public Map<String, List<UserTicket>> process(UserTicket item) throws Exception {
                String key = item.getMemberId() + "_" + item.getTicket().getCenterId();
                ticketsByMemberAndCenter.computeIfAbsent(key, k -> new ArrayList<>()).add(item);
                return ticketsByMemberAndCenter;
            }
        };
    }

    @Bean
    public ItemWriter<Map<String, List<UserTicket>>> expiredCenterMembershipWriter() {
        return items -> {
            for (Map<String, List<UserTicket>> expiredMemberships : items) {
                for (Map.Entry<String, List<UserTicket>> entry : expiredMemberships.entrySet()) {
                    List<UserTicket> userTickets = entry.getValue();
                    if (userTickets.stream().noneMatch(ticket ->
                            ticket.getEndDate().isAfter(LocalDate.now()) && ticket.getRemainingCount() > 0)) {
                        String[] memberAndCenter = entry.getKey().split("_");
                        Long memberId = Long.valueOf(memberAndCenter[0]);
                        Long centerId = Long.valueOf(memberAndCenter[1]);

                        CenterMembership membership = centerMembershipRepository.findByMemberIdAndCenterId(memberId, centerId);
                        if (membership != null) {
                            membership.setStatus(CenterMembershipStatusEnum.EXPIRED.getStatusType());
                            membership.setUpdatedBy("batch");
                            centerMembershipRepository.save(membership);
                        }
                    }
                }
            }
        };
    }
}
