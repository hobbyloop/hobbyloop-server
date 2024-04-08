package com.example.ticketservice.ticket.acceptance;

import com.example.ticketservice.AcceptanceTest;
import com.example.ticketservice.MemberFixture;
import com.example.ticketservice.client.MemberServiceClient;
import com.example.ticketservice.dto.response.*;
import com.example.ticketservice.fixture.CenterFixture;
import com.example.ticketservice.fixture.ReviewFixture;
import com.example.ticketservice.fixture.TicketFixture;
import com.example.ticketservice.client.CompanyServiceClient;
import com.example.ticketservice.dto.BaseResponseDto;
import com.example.ticketservice.service.AmazonS3Service;
import com.example.ticketservice.ticket.utils.AdminTicketSteps;
import com.example.ticketservice.ticket.utils.ReviewSteps;
import com.example.ticketservice.ticket.utils.TicketSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TicketAcceptanceTest extends AcceptanceTest {
    @MockBean
    private CompanyServiceClient companyServiceClient;

    @MockBean
    private MemberServiceClient memberServiceClient;

    @MockBean
    private AmazonS3Service amazonS3Service;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void createTicketSuccess() throws IOException {
        // given
        long centerId = 1L;

        // when
        mockForCreateTicket();

        TicketCreateResponseDto response = AdminTicketSteps.createTicket(centerId, TicketFixture.defaultTicketCreateRequest());

        // then
        assertThat(response.getTicketImageUrl()).isEqualTo("test-image-url");
    }

    @Test
    public void getAdminTicketManagementList() throws IOException {
        // given
        long centerId = 1L;

        mockForCreateTicket();

        // when
        int pageSize = 20;
        for (int i = 0; i < pageSize + 1; i++) {
            AdminTicketSteps.createTicket(centerId, TicketFixture.defaultTicketCreateRequest());
        }

        // then
        List<TicketResponseDto> response = AdminTicketSteps.getTicketManagementList(centerId, pageSize + 1);

        assertThat(response.size()).isEqualTo(pageSize);
    }

    @Test
    public void createTicketReview() throws Exception {
        // given
        long centerId = 1L;
        long ticketId = 1L;

        mockForCreateTicket();
        AdminTicketSteps.createTicket(centerId, TicketFixture.defaultTicketCreateRequest());

        // when
        Long reviewId = ReviewSteps.createReview(ticketId, ReviewFixture.normalReviewCreateRequest());
        AdminReviewTicketResponseDto ticket = AdminTicketSteps.getTicketDetailWithReview(ticketId);

        // then
        assertThat(reviewId).isNotNull();
        assertThat(ticket.getScore()).isEqualTo(ReviewFixture.NORMAL_SCORE);
    }

    @Test
    public void purchaseTicketSuccess() throws Exception {
        // given
        long centerId = 1L;

        mockForCreateTicket();
        long ticketId = AdminTicketSteps.createTicket(centerId, TicketFixture.defaultTicketCreateRequest()).getTicketId();
        AdminTicketSteps.uploadTicket(ticketId);

        // when
        mockForGetTicketDetail();
        Long userTicketId = TicketSteps.purchaseTicket(ticketId);

        // then
        assertThat(userTicketId).isNotNull();
    }

    @Test
    public void getUnapprovedUserTicketListSuccess() throws Exception {
        // given
        long centerId = 1L;

        mockForCreateTicket();
        long ticketId = AdminTicketSteps.createTicket(centerId, TicketFixture.defaultTicketCreateRequest()).getTicketId();
        AdminTicketSteps.uploadTicket(ticketId);
        Long userTicketId = TicketSteps.purchaseTicket(ticketId);

        // when
        given(memberServiceClient.getMemberInfo(anyLong())).willReturn(new BaseResponseDto<>(MemberFixture.defaultMemberInfoResponse()));
        List<UnapprovedUserTicketListResponseDto> response = AdminTicketSteps.getUnapprovedUserTicketList(centerId);

        // then
        assertThat(response.size()).isEqualTo(1);
        assertThat(response.get(0).getUserTicketId()).isEqualTo(userTicketId);
    }

    private void mockForCreateTicket() throws IOException {
        given(companyServiceClient.getCenterInfo(anyLong())).willReturn(new BaseResponseDto<>(CenterFixture.defaultCenterInfoResponseDto()));
        given(amazonS3Service.upload(any(MultipartFile.class), anyString())).willReturn("test-image-key");
        given(amazonS3Service.getFileUrl("test-image-key")).willReturn("test-image-url");
    }

    private void mockForGetTicketDetail() throws Exception {
        given(companyServiceClient.getOriginalCenterInfo(anyLong())).willReturn(new BaseResponseDto<>(CenterFixture.defaultOriginalCenterResponseDto()));
        given(companyServiceClient.getOriginalBusinessInfo(anyLong())).willReturn(new BaseResponseDto<>(CenterFixture.defaultOriginalBusinessResponseDto()));
    }

}