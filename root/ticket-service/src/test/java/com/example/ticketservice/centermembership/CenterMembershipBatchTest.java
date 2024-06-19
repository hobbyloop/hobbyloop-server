package com.example.ticketservice.centermembership;

import com.example.ticketservice.AcceptanceTest;
import com.example.ticketservice.ticket.client.CompanyServiceClient;
import com.example.ticketservice.ticket.client.MemberServiceClient;
import com.example.ticketservice.ticket.dto.BaseResponseDto;
import com.example.ticketservice.ticket.entity.CenterMembership;
import com.example.ticketservice.ticket.entity.CenterMembershipStatusEnum;
import com.example.ticketservice.ticket.entity.UserTicket;
import com.example.ticketservice.fixture.CenterFixture;
import com.example.ticketservice.fixture.MemberFixture;
import com.example.ticketservice.fixture.TicketFixture;
import com.example.ticketservice.ticket.repository.centermembership.CenterMembershipRepository;
import com.example.ticketservice.ticket.repository.ticket.UserTicketRepository;
import com.example.ticketservice.ticket.service.AmazonS3Service;
import com.example.ticketservice.ticket.utils.AdminTicketSteps;
import com.example.ticketservice.ticket.utils.UserTicketSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBatchTest
public class CenterMembershipBatchTest extends AcceptanceTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    @Qualifier("expiringSoonCenterMembershipJob")
    private Job expiringSoonCenterMembershipJob;

    @Autowired
    @Qualifier("expiredCenterMembershipJob")
    private Job expiredCenterMembershipJob;

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
        super.setUp();

        mockForCreateTicket();
        pilatesTicketId = AdminTicketSteps.createTicket(pilatesCenterId, TicketFixture.defaultTicketCreateRequest()).getTicketId();
        pilatesTicketId2 = AdminTicketSteps.createTicket(pilatesCenterId, TicketFixture.defaultTicketCreateRequest()).getTicketId();
        bakingTicketId = AdminTicketSteps.createTicket(bakingCenterId, TicketFixture.defaultTicketCreateRequest()).getTicketId();

        AdminTicketSteps.uploadTicket(pilatesTicketId);
        AdminTicketSteps.uploadTicket(pilatesTicketId2);
        AdminTicketSteps.uploadTicket(bakingTicketId);
    }

    @Test
    public void expiringSoonCenterMembershipJob() throws Exception {
        // given
        Long activeMemberId = 1L;
        Long expiringSoonMemberId = 2L;

        mockForPurchaseTicket();
        Long activeBakingTicketId = UserTicketSteps.purchaseTicket(bakingTicketId, activeMemberId);

        Long activePilatesTicketId = UserTicketSteps.purchaseTicket(pilatesTicketId, expiringSoonMemberId);
        Long expiringSoonPilatesTicketId = UserTicketSteps.purchaseTicket(pilatesTicketId2, expiringSoonMemberId);

        CenterMembershipSteps.approveUserTicket(activeBakingTicketId);
        CenterMembershipSteps.approveUserTicket(activePilatesTicketId);
        CenterMembershipSteps.approveUserTicket(expiringSoonPilatesTicketId);

        LocalDate now = LocalDate.now();
        UserTicket expiringSoonPilatesTicket = userTicketRepository.findById(expiringSoonPilatesTicketId).get();
        ReflectionTestUtils.setField(expiringSoonPilatesTicket, "endDate", now.plusDays(25));
        userTicketRepository.save(expiringSoonPilatesTicket);

        // when
        jobLauncherTestUtils.setJob(expiringSoonCenterMembershipJob);
        JobExecution execution = jobLauncherTestUtils.launchJob();

        // then
        CenterMembership activeCenterMembership = centerMembershipRepository.findByMemberIdAndCenterId(activeMemberId, bakingCenterId);
        CenterMembership expiringSoonCenterMembership = centerMembershipRepository.findByMemberIdAndCenterId(expiringSoonMemberId, pilatesCenterId);

        assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(activeCenterMembership.getStatus()).isEqualTo(CenterMembershipStatusEnum.ACTIVE.getStatusType());
        assertThat(expiringSoonCenterMembership.getStatus()).isEqualTo(CenterMembershipStatusEnum.EXPIRING_SOON.getStatusType());
        assertThat(expiringSoonCenterMembership.getUpdatedBy()).isEqualTo("batch");
    }

    @Test
    public void expiredCenterMembershipJob() throws Exception {
        // given
        Long activeMemberId = 1L;
        Long expiredMemberId = 2L;

        mockForPurchaseTicket();
        Long expiredPilatesTicketId = UserTicketSteps.purchaseTicket(pilatesTicketId, activeMemberId);
        Long activePilatesTicketId = UserTicketSteps.purchaseTicket(pilatesTicketId2, activeMemberId);
        Long expiredBakingTicketId = UserTicketSteps.purchaseTicket(bakingTicketId, expiredMemberId);

        CenterMembershipSteps.approveUserTicket(expiredPilatesTicketId);
        CenterMembershipSteps.approveUserTicket(activePilatesTicketId);
        CenterMembershipSteps.approveUserTicket(expiredBakingTicketId);

        UserTicket pilatesTicket = userTicketRepository.findById(expiredPilatesTicketId).get();
        UserTicket bakingTicket = userTicketRepository.findById(expiredBakingTicketId).get();

        LocalDate now = LocalDate.now();
        ReflectionTestUtils.setField(pilatesTicket, "endDate", now.minusDays(1));
        ReflectionTestUtils.setField(bakingTicket, "endDate", now.minusDays(1));
        userTicketRepository.saveAll(Arrays.asList(pilatesTicket, bakingTicket));

        // when
        jobLauncherTestUtils.setJob(expiredCenterMembershipJob);
        JobExecution execution = jobLauncherTestUtils.launchJob();

        // then
        CenterMembership activeCenterMembership = centerMembershipRepository.findByMemberIdAndCenterId(activeMemberId, pilatesCenterId);
        CenterMembership expiredCenterMembership = centerMembershipRepository.findByMemberIdAndCenterId(expiredMemberId, bakingCenterId);

        assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(expiredCenterMembership.getStatus()).isEqualTo(CenterMembershipStatusEnum.EXPIRED.getStatusType());
        assertThat(activeCenterMembership.getStatus()).isEqualTo(CenterMembershipStatusEnum.ACTIVE.getStatusType());
    }

    @Test
    public void renewAfterExpiring() throws Exception {
        // given
        Long memberId = 1L;

        mockForPurchaseTicket();
        Long expiredPilatesTicketId = UserTicketSteps.purchaseTicket(pilatesTicketId, memberId);

        CenterMembershipSteps.approveUserTicket(expiredPilatesTicketId);

        UserTicket pilatesTicket = userTicketRepository.findById(expiredPilatesTicketId).get();

        LocalDate now = LocalDate.now();
        ReflectionTestUtils.setField(pilatesTicket, "endDate", now.minusDays(1));
        userTicketRepository.save(pilatesTicket);

        jobLauncherTestUtils.setJob(expiredCenterMembershipJob);
        JobExecution execution = jobLauncherTestUtils.launchJob();

        CenterMembership expiredCenterMembership = centerMembershipRepository.findByMemberIdAndCenterId(memberId, pilatesCenterId);
        assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(expiredCenterMembership.getStatus()).isEqualTo(CenterMembershipStatusEnum.EXPIRED.getStatusType());

        // when
        Long activePilatesTicketId = UserTicketSteps.purchaseTicket(pilatesTicketId2, memberId);

        CenterMembershipSteps.approveUserTicket(activePilatesTicketId);

        // then
        CenterMembership activeCenterMembership = centerMembershipRepository.findByMemberIdAndCenterId(memberId, pilatesCenterId);
        assertThat(activeCenterMembership.getStatus()).isEqualTo(CenterMembershipStatusEnum.RENEWED.getStatusType());
    }

    private void mockForCreateTicket() {
        given(companyServiceClient.getCenterInfo(pilatesCenterId)).willReturn(new BaseResponseDto<>(CenterFixture.defaultCenterInfoResponseDto()));
        given(companyServiceClient.getCenterInfo(bakingCenterId)).willReturn(new BaseResponseDto<>(CenterFixture.nonRefundableCenterInfoResponseDto()));
        given(amazonS3Service.saveS3Img(any(MultipartFile.class), anyString())).willReturn("test-image-key");
        given(amazonS3Service.getFileUrl("test-image-key")).willReturn("test-image-url");
    }

    private void mockForPurchaseTicket() {
        given(memberServiceClient.getMemberInfo(anyLong())).willReturn(new BaseResponseDto<>(MemberFixture.defaultMemberInfoResponse()));
    }
}
