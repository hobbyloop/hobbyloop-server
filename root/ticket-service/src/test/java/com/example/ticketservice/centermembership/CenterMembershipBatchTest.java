package com.example.ticketservice.centermembership;

import com.example.ticketservice.DatabaseCleanup;
import com.example.ticketservice.client.CompanyServiceClient;
import com.example.ticketservice.client.MemberServiceClient;
import com.example.ticketservice.dto.BaseResponseDto;
import com.example.ticketservice.entity.CenterMembership;
import com.example.ticketservice.entity.CenterMembershipStatusEnum;
import com.example.ticketservice.entity.Ticket;
import com.example.ticketservice.entity.UserTicket;
import com.example.ticketservice.fixture.CenterFixture;
import com.example.ticketservice.fixture.MemberFixture;
import com.example.ticketservice.fixture.TicketFixture;
import com.example.ticketservice.fixture.UserTicketFixture;
import com.example.ticketservice.repository.centermembership.CenterMembershipRepository;
import com.example.ticketservice.repository.ticket.TicketRepository;
import com.example.ticketservice.repository.ticket.UserTicketRepository;
import com.example.ticketservice.service.AmazonS3Service;
import com.example.ticketservice.service.TicketService;
import com.example.ticketservice.service.UserTicketService;
import com.example.ticketservice.ticket.utils.AdminTicketSteps;
import com.example.ticketservice.ticket.utils.UserTicketSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBatchTest
@SpringBootTest
@ActiveProfiles("test")
public class CenterMembershipBatchTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserTicketRepository userTicketRepository;

    @MockBean
    private CompanyServiceClient companyServiceClient;

    @MockBean
    private MemberServiceClient memberServiceClient;

    @MockBean
    private AmazonS3Service amazonS3Service;

    @Autowired
    private CenterMembershipRepository centerMembershipRepository;

    Long pilatesCenterId = 1L;

    Long bakingCenterId = 2L;

    Long pilatesTicketId;

    Long pilatesTicketId2;

    Long bakingTicketId;

    @BeforeEach
    public void setUp() throws Exception {
        databaseCleanup.execute();

        mockForCreateTicket();
        pilatesTicketId = AdminTicketSteps.createTicket(pilatesCenterId, TicketFixture.defaultTicketCreateRequest()).getTicketId();
        pilatesTicketId2 = AdminTicketSteps.createTicket(pilatesCenterId, TicketFixture.defaultTicketCreateRequest()).getTicketId();
        bakingTicketId = AdminTicketSteps.createTicket(bakingCenterId, TicketFixture.defaultTicketCreateRequest()).getTicketId();

        AdminTicketSteps.uploadTicket(pilatesTicketId);
        AdminTicketSteps.uploadTicket(pilatesTicketId2);
        AdminTicketSteps.uploadTicket(bakingTicketId);
    }

    @Test
    public void expiredCenterMembershipJob() throws Exception {
        // given
        Long activeMemberId = 1L;
        Long expiredMemberId = 2L;

        mockForPurchaseTicket();
        UserTicketSteps.purchaseTicket(pilatesTicketId, activeMemberId);
        UserTicketSteps.purchaseTicket(pilatesTicketId2, activeMemberId);
        UserTicketSteps.purchaseTicket(bakingTicketId, expiredMemberId);

        UserTicket pilatesTicket = userTicketRepository.findById(pilatesTicketId).get();
        UserTicket bakingTicket = userTicketRepository.findById(bakingTicketId).get();

        LocalDate now = LocalDate.now();
        ReflectionTestUtils.setField(pilatesTicket, "endDate", now.minusDays(1));
        ReflectionTestUtils.setField(bakingTicket, "endDate", now.minusDays(1));
        userTicketRepository.saveAll(Arrays.asList(pilatesTicket, bakingTicket));

        // when
        JobExecution execution = jobLauncherTestUtils.launchJob();

        // then
        CenterMembership activeCenterMembership = centerMembershipRepository.findByMemberIdAndCenterId(activeMemberId, pilatesCenterId);
        CenterMembership expiredCenterMembership = centerMembershipRepository.findByMemberIdAndCenterId(expiredMemberId, bakingCenterId);

        assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(expiredCenterMembership.getStatus()).isEqualTo(CenterMembershipStatusEnum.EXPIRED.getStatusType());
    }

    private void mockForCreateTicket() throws IOException {
        given(companyServiceClient.getCenterInfo(pilatesCenterId)).willReturn(new BaseResponseDto<>(CenterFixture.defaultCenterInfoResponseDto()));
        given(companyServiceClient.getCenterInfo(bakingCenterId)).willReturn(new BaseResponseDto<>(CenterFixture.nonRefundableCenterInfoResponseDto()));
        given(amazonS3Service.upload(any(MultipartFile.class), anyString())).willReturn("test-image-key");
        given(amazonS3Service.getFileUrl("test-image-key")).willReturn("test-image-url");
    }

    private void mockForPurchaseTicket() {
        given(memberServiceClient.getMemberInfo(anyLong())).willReturn(new BaseResponseDto<>(MemberFixture.defaultMemberInfoResponse()));
    }
}
