package com.example.ticketservice.centermembership;

import com.example.ticketservice.AcceptanceTest;
import com.example.ticketservice.client.CompanyServiceClient;
import com.example.ticketservice.client.MemberServiceClient;
import com.example.ticketservice.dto.BaseResponseDto;
import com.example.ticketservice.dto.response.CenterMemberResponseDto;
import com.example.ticketservice.dto.response.CenterMembershipJoinedResponseDto;
import com.example.ticketservice.dto.response.TicketDetailResponseDto;
import com.example.ticketservice.dto.response.UnapprovedUserTicketListResponseDto;
import com.example.ticketservice.fixture.CenterFixture;
import com.example.ticketservice.fixture.CenterMembershipFixture;
import com.example.ticketservice.fixture.MemberFixture;
import com.example.ticketservice.fixture.TicketFixture;
import com.example.ticketservice.service.AmazonS3Service;
import com.example.ticketservice.ticket.utils.AdminTicketSteps;
import com.example.ticketservice.ticket.utils.UserTicketSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

public class CenterMembershipAcceptanceTest extends AcceptanceTest {
    @MockBean
    private CompanyServiceClient companyServiceClient;

    @MockBean
    private MemberServiceClient memberServiceClient;

    @MockBean
    private AmazonS3Service amazonS3Service;

    long centerId = 1L;
    long ticketId1;
    long ticketId2;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        given(companyServiceClient.getCenterInfo(centerId)).willReturn(new BaseResponseDto<>(CenterFixture.defaultCenterInfoResponseDto()));
        given(amazonS3Service.upload(any(MultipartFile.class), anyString())).willReturn("test-image-key");
        given(amazonS3Service.getFileUrl("test-image-key")).willReturn("test-image-url");

        ticketId1 = AdminTicketSteps.createTicket(centerId, TicketFixture.defaultTicketCreateRequest()).getTicketId();
        ticketId2 = AdminTicketSteps.createTicket(centerId, TicketFixture.defaultTicketCreateRequest()).getTicketId();

        AdminTicketSteps.uploadTicket(ticketId1);
        AdminTicketSteps.uploadTicket(ticketId2);
    }

    @Test
    public void joinCenterMembershipByAdminSuccess() throws Exception {
        // given & when
        CenterMembershipJoinedResponseDto response = CenterMembershipSteps.joinCenterMembershipByAdmin(centerId, CenterMembershipFixture.defaultCenterMembershipJoinRequest(ticketId1));

        // then
        mockForGetTicketDetail();
        TicketDetailResponseDto ticket = AdminTicketSteps.getTicketDetail(ticketId1);
        assertThat(ticket.getIssueCount()).isEqualTo(1);

        List<CenterMemberResponseDto> centerMembers = CenterMembershipSteps.getCenterMemberList(centerId, 0, 0);
        assertThat(centerMembers.size()).isEqualTo(1);
    }

    @Test
    public void approveUserTicketSuccess() throws Exception {
        // given
        UserTicketSteps.purchaseTicket(ticketId1);

        given(memberServiceClient.getMemberInfo(anyLong())).willReturn(new BaseResponseDto<>(MemberFixture.defaultMemberInfoResponse()));
        Long userTicketId = CenterMembershipSteps.getUnapprovedUserTicketList(centerId).get(0).getUserTicketId();

        // when
        CenterMembershipSteps.approveUserTicket(userTicketId);

        // then
        mockForGetTicketDetail();
        TicketDetailResponseDto ticket = AdminTicketSteps.getTicketDetail(ticketId1);
        assertThat(ticket.getIssueCount()).isEqualTo(1);
    }

    @Test
    public void getUnapprovedUserTicketListSuccess() throws Exception {
        // given
        given(memberServiceClient.getMemberInfo(anyLong())).willReturn(new BaseResponseDto<>(MemberFixture.defaultMemberInfoResponse()));

        Long unapprovedUserTicketId = UserTicketSteps.purchaseTicket(ticketId1);
        Long approvedUserTicketId = UserTicketSteps.purchaseTicket(ticketId2);
        CenterMembershipSteps.approveUserTicket(approvedUserTicketId);

        // when
        List<UnapprovedUserTicketListResponseDto> response = CenterMembershipSteps.getUnapprovedUserTicketList(centerId);

        // then
        assertThat(response.size()).isEqualTo(1);
        assertThat(response.get(0).getUserTicketId()).isEqualTo(unapprovedUserTicketId);
    }

    @Test
    public void getCenterMemberListSuccess() throws Exception {
        // given
        given(memberServiceClient.getMemberInfo(anyLong())).willReturn(new BaseResponseDto<>(MemberFixture.defaultMemberInfoResponse()));

        Long oldTicketId = UserTicketSteps.purchaseTicket(ticketId1);
        Long newTicketId = UserTicketSteps.purchaseTicket(ticketId2);
        CenterMembershipSteps.approveUserTicket(oldTicketId);
        CenterMembershipSteps.approveUserTicket(newTicketId);

        // when
        List<CenterMemberResponseDto> responses = CenterMembershipSteps.getCenterMemberList(centerId, 0, 0);

        // then
        assertThat(responses.size()).isEqualTo(1);
    }

    private void mockForGetTicketDetail() {
        given(companyServiceClient.getOriginalCenterInfo(anyLong())).willReturn(new BaseResponseDto<>(CenterFixture.defaultOriginalCenterResponseDto()));
        given(companyServiceClient.getOriginalBusinessInfo(anyLong())).willReturn(new BaseResponseDto<>(CenterFixture.defaultOriginalBusinessResponseDto()));

    }
}