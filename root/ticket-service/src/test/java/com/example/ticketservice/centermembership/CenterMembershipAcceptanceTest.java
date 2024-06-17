package com.example.ticketservice.centermembership;

import com.example.ticketservice.AcceptanceTest;
import com.example.ticketservice.ticket.client.CompanyServiceClient;
import com.example.ticketservice.ticket.client.MemberServiceClient;
import com.example.ticketservice.ticket.dto.BaseResponseDto;
import com.example.ticketservice.ticket.dto.response.*;
import com.example.ticketservice.fixture.CenterFixture;
import com.example.ticketservice.fixture.CenterMembershipFixture;
import com.example.ticketservice.fixture.MemberFixture;
import com.example.ticketservice.fixture.TicketFixture;
import com.example.ticketservice.ticket.dto.response.centermembership.CenterMemberResponseDto;
import com.example.ticketservice.ticket.dto.response.centermembership.CenterMembershipDetailResponseDto;
import com.example.ticketservice.ticket.dto.response.centermembership.CenterMembershipJoinedResponseDto;
import com.example.ticketservice.ticket.dto.response.userticket.UnapprovedUserTicketListResponseDto;
import com.example.ticketservice.ticket.service.AmazonS3Service;
import com.example.ticketservice.ticket.utils.AdminTicketSteps;
import com.example.ticketservice.ticket.utils.UserTicketSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    long otherCenterId = 2L;
    long ticketId1;
    long ticketId2;
    long ticketIdOfOtherCenter;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        given(companyServiceClient.getCenterInfo(centerId)).willReturn(new BaseResponseDto<>(CenterFixture.defaultCenterInfoResponseDto()));
        given(companyServiceClient.getCenterInfo(otherCenterId)).willReturn(new BaseResponseDto<>(CenterFixture.defaultCenterInfoResponseDto()));
        given(amazonS3Service.upload(any(MultipartFile.class), anyString())).willReturn("test-image-key");
        given(amazonS3Service.getFileUrl("test-image-key")).willReturn("test-image-url");

        ticketId1 = AdminTicketSteps.createTicket(centerId, TicketFixture.defaultTicketCreateRequest()).getTicketId();
        ticketId2 = AdminTicketSteps.createTicket(centerId, TicketFixture.defaultTicketCreateRequest()).getTicketId();
        ticketIdOfOtherCenter = AdminTicketSteps.createTicket(otherCenterId, TicketFixture.defaultTicketCreateRequest()).getTicketId();

        AdminTicketSteps.uploadTicket(ticketId1);
        AdminTicketSteps.uploadTicket(ticketId2);
        AdminTicketSteps.uploadTicket(ticketIdOfOtherCenter);
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
        mockForPurchaseTicket();
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

        mockForPurchaseTicket();
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

        mockForPurchaseTicket();
        Long oldTicketId = UserTicketSteps.purchaseTicket(ticketId1);
        Long newTicketId = UserTicketSteps.purchaseTicket(ticketId2);
        CenterMembershipSteps.approveUserTicket(oldTicketId);
        CenterMembershipSteps.approveUserTicket(newTicketId);

        // when
        List<CenterMemberResponseDto> responses = CenterMembershipSteps.getCenterMemberList(centerId, 0, 0);

        // then
        assertThat(responses.size()).isEqualTo(1);
    }

    @Test
    public void getCenterMembershipDetailSuccess() throws Exception {
        // given
        given(memberServiceClient.getMemberInfo(anyLong())).willReturn(new BaseResponseDto<>(MemberFixture.defaultMemberInfoResponse()));
        mockForPurchaseTicket();

        Long userTicketId = UserTicketSteps.purchaseTicket(ticketId1);
        Long userTicketId2 = UserTicketSteps.purchaseTicket(ticketIdOfOtherCenter);
        CenterMembershipSteps.approveUserTicket(userTicketId);
        CenterMembershipSteps.approveUserTicket(userTicketId2);

        // when
        CenterMembershipDetailResponseDto response = CenterMembershipSteps.getCenterMembershipDetail(1L);

        // then
        assertThat(response.getTickets().size()).isEqualTo(1);

    }

    private void mockForGetTicketDetail() {
        given(companyServiceClient.getOriginalCenterInfo(anyLong())).willReturn(new BaseResponseDto<>(CenterFixture.defaultOriginalCenterResponseDto()));
        given(companyServiceClient.getOriginalBusinessInfo(anyLong())).willReturn(new BaseResponseDto<>(CenterFixture.defaultOriginalBusinessResponseDto()));
    }

    private void mockForPurchaseTicket() throws IOException {
        given(companyServiceClient.getOriginalCenterInfo(anyLong())).willReturn(new BaseResponseDto<>(CenterFixture.defaultOriginalCenterResponseDto()));
    }
}
