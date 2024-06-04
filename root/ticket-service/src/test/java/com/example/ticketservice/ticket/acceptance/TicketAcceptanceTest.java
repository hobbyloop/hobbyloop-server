package com.example.ticketservice.ticket.acceptance;

import com.example.ticketservice.AcceptanceTest;
import com.example.ticketservice.centermembership.CenterMembershipSteps;
import com.example.ticketservice.lecturereservation.LectureReservationSteps;
import com.example.ticketservice.ticket.client.MemberServiceClient;
import com.example.ticketservice.fixture.CenterFixture;
import com.example.ticketservice.fixture.ReviewFixture;
import com.example.ticketservice.fixture.TicketFixture;
import com.example.ticketservice.ticket.client.CompanyServiceClient;
import com.example.ticketservice.ticket.dto.BaseResponseDto;
import com.example.ticketservice.ticket.dto.response.*;
import com.example.ticketservice.ticket.service.AmazonS3Service;
import com.example.ticketservice.ticket.utils.AdminTicketSteps;
import com.example.ticketservice.ticket.utils.ReviewSteps;
import com.example.ticketservice.ticket.utils.TicketSteps;
import com.example.ticketservice.ticket.utils.UserTicketSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

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
    public void createTicketReviewSuccess() throws Exception {
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
    public void getSortedTicketReviewListByScoreSuccess() throws Exception {
        // given
        long centerId = 1L;

        mockForCreateTicket();
        long ticketId = AdminTicketSteps.createTicket(centerId, TicketFixture.defaultTicketCreateRequest()).getTicketId();

        ReviewSteps.createReview(ticketId, ReviewFixture.normalReviewCreateRequest());
        ReviewSteps.createReview(ticketId, ReviewFixture.goodReviewCreateRequest());
        ReviewSteps.createReview(ticketId, ReviewFixture.badReviewCreateRequest());

        // when
        List<ReviewResponseDto> responses = ReviewSteps.getReviewList(ticketId, 0, ReviewFixture.SORT_BY_SCORE_DESC);

        // then
        assertThat(responses.size()).isEqualTo(3);
        assertThat(responses.get(0).getScore()).isEqualTo(ReviewFixture.GOOD_SCORE);
        assertThat(responses.get(1).getScore()).isEqualTo(ReviewFixture.NORMAL_SCORE);
        assertThat(responses.get(2).getScore()).isEqualTo(ReviewFixture.BAD_SCORE);
    }

    @Test
    public void getSortedTicketReviewListByCreatedAtSuccess() throws Exception {
        // given
        long centerId = 1L;

        mockForCreateTicket();
        long ticketId = AdminTicketSteps.createTicket(centerId, TicketFixture.defaultTicketCreateRequest()).getTicketId();

        ReviewSteps.createReview(ticketId, ReviewFixture.normalReviewCreateRequest());
        ReviewSteps.createReview(ticketId, ReviewFixture.goodReviewCreateRequest());
        ReviewSteps.createReview(ticketId, ReviewFixture.badReviewCreateRequest());

        // when
        List<ReviewResponseDto> responses = ReviewSteps.getReviewList(ticketId, 0, ReviewFixture.SORT_BY_REVIEW_ID_DESC);

        // then
        assertThat(responses.size()).isEqualTo(3);
        assertThat(responses.get(0).getScore()).isEqualTo(ReviewFixture.BAD_SCORE);
        assertThat(responses.get(1).getScore()).isEqualTo(ReviewFixture.GOOD_SCORE);
        assertThat(responses.get(2).getScore()).isEqualTo(ReviewFixture.NORMAL_SCORE);
    }

    @Test
    public void getReviewListByCenterSuccess() throws Exception {
        // given
        long centerId = 1L;

        mockForCreateTicket();
        long ticketId = AdminTicketSteps.createTicket(centerId, TicketFixture.defaultTicketCreateRequest()).getTicketId();
        ReviewSteps.createReview(ticketId, ReviewFixture.normalReviewCreateRequest());
        ReviewSteps.createReview(ticketId, ReviewFixture.goodReviewCreateRequest());

        long ticketId2 = AdminTicketSteps.createTicket(centerId, TicketFixture.defaultTicketCreateRequest()).getTicketId();
        ReviewSteps.createReview(ticketId2, ReviewFixture.badReviewCreateRequest());

        // when
        TicketReviewListByCenterResponseDto response = ReviewSteps.getReviewListByCenter(centerId, 0, ReviewFixture.SORT_BY_SCORE_DESC);

        // then
        assertThat(response.getReviewList().size()).isEqualTo(3);
        assertThat(response.getReviewList().get(0).getScore()).isEqualTo(ReviewFixture.GOOD_SCORE);
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
        Long userTicketId = UserTicketSteps.purchaseTicket(ticketId);

        // then
        assertThat(userTicketId).isNotNull();
    }

    @Test
    public void getAvailableUserTicketListSuccess() throws Exception {
        // given
        long defaultCenterId = 1L;
        long nonRefundableCenterId = 2L;

        mockForCreateTicket();
        long ticketIdOfDefaultCenter = AdminTicketSteps.createTicket(defaultCenterId, TicketFixture.defaultTicketCreateRequest()).getTicketId();
        AdminTicketSteps.uploadTicket(ticketIdOfDefaultCenter);
        long ticketIdOfDefaultCenter2 = AdminTicketSteps.createTicket(defaultCenterId, TicketFixture.defaultTicketCreateRequest()).getTicketId();
        AdminTicketSteps.uploadTicket(ticketIdOfDefaultCenter2);

        long ticketIdOfNonRefundableCenter = AdminTicketSteps.createTicket(nonRefundableCenterId, TicketFixture.defaultTicketCreateRequest()).getTicketId();
        AdminTicketSteps.uploadTicket(ticketIdOfNonRefundableCenter);

        mockForPurchaseTicket();
        long userTicketId1 = UserTicketSteps.purchaseTicket(ticketIdOfDefaultCenter);
        long userTicketId2 = UserTicketSteps.purchaseTicket(ticketIdOfNonRefundableCenter);
        long userTicketId3 = UserTicketSteps.purchaseTicket(ticketIdOfDefaultCenter2);

        CenterMembershipSteps.approveUserTicket(userTicketId1);
        CenterMembershipSteps.approveUserTicket(userTicketId2);
        CenterMembershipSteps.approveUserTicket(userTicketId3);

        // when
        Map<String, AvailableUserTicketsWithCenterInfo> response = UserTicketSteps.getMyAvailableUserTicketList();

        // then
        assertThat(response.size()).isEqualTo(2);
        assertThat(response.containsKey(CenterFixture.DEFAULT_CENTER_NAME)).isTrue();
        assertThat(response.containsKey(CenterFixture.NON_REFUNDABLE_CENTER_NAME)).isTrue();
        assertThat(response.get(CenterFixture.DEFAULT_CENTER_NAME).getAvailableUserTickets().size()).isEqualTo(2);

    }

    @Test
    public void getRecentPurchaseUserTicketListSuccess() throws Exception {
        // given
        long centerId = 1L;

        mockForCreateTicket();
        long ticketId = AdminTicketSteps.createTicket(centerId, TicketFixture.defaultTicketCreateRequest()).getTicketId();
        AdminTicketSteps.uploadTicket(ticketId);

        // when
        mockForGetTicketDetail();

        for (int i = 0; i < 4; i++) {
            UserTicketSteps.purchaseTicket(ticketId);
        }

        Map<YearMonth, List<RecentPurchaseUserTicketListResponseDto>> response = UserTicketSteps.getMyRecentPurchaseUserTicketList();

        // then
        LocalDateTime now = LocalDateTime.now();
        int thisYear = now.getYear();
        int thisMonth = now.getMonthValue();

        assertThat(response.size()).isEqualTo(1);
        assertThat(response.containsKey(YearMonth.of(thisYear, thisMonth))).isTrue();
    }

    @Test
    public void getUserTicketUsingHistoriesSuccess() throws Exception {
        // given
        long centerId = 1L;

        mockForCreateTicket();
        mockForPurchaseTicket();
        long ticketId = AdminTicketSteps.createTicket(centerId, TicketFixture.defaultTicketCreateRequest()).getTicketId();
        AdminTicketSteps.uploadTicket(ticketId);
        long userTicketId = UserTicketSteps.purchaseTicket(ticketId);

        // when
        LectureReservationSteps.reserveLecture(userTicketId);

        List<UserTicketUsingHistoryResponseDto> response = UserTicketSteps.getUserTicketUsingHistory();

        // then
        String yearMonth = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM"));
        assertThat(response.get(0).getUsingHistoryByMonth().get(0).getYearMonth()).isEqualTo(yearMonth);
    }

    @Test
    public void getTicketListByCenterSuccess() throws Exception {
        // given
        long centerId = 1L;

        mockForCreateTicket();
        long defaultTicketId = AdminTicketSteps.createTicket(centerId, TicketFixture.defaultTicketCreateRequest()).getTicketId();
        AdminTicketSteps.uploadTicket(defaultTicketId);

        long mediumTicketId = AdminTicketSteps.createTicket(centerId, TicketFixture.mediumPriceTicketCreateRequest()).getTicketId();
        AdminTicketSteps.uploadTicket(mediumTicketId);

        long highTicketId = AdminTicketSteps.createTicket(centerId, TicketFixture.highPriceTicketCreateRequest()).getTicketId();
        AdminTicketSteps.uploadTicket(highTicketId);

        // when
        List<TicketByCenterResponseDto> responses = TicketSteps.getTicketListByCenter(centerId);

        // then
        assertThat(responses.size()).isEqualTo(3);
        assertThat(responses.get(0).getTicketId()).isEqualTo(defaultTicketId);
        assertThat(responses.get(1).getTicketId()).isEqualTo(mediumTicketId);
        assertThat(responses.get(2).getTicketId()).isEqualTo(highTicketId);
    }

    @Test
    public void getMyTicketListSuccess() throws Exception {
        // given
        long centerId = 1L;

        mockForCreateTicket();
        long ticketId = AdminTicketSteps.createTicket(centerId, TicketFixture.defaultTicketCreateRequest()).getTicketId();
        AdminTicketSteps.uploadTicket(ticketId);

        AdminTicketSteps.createTicket(centerId, TicketFixture.defaultTicketCreateRequest());

        long ticketId3 = AdminTicketSteps.createTicket(centerId, TicketFixture.defaultTicketCreateRequest()).getTicketId();
        AdminTicketSteps.uploadTicket(ticketId3);

        // when
        List<AdminMyTicketResponseDto> responses = AdminTicketSteps.getMyTicketList(centerId);

        // then
        assertThat(responses.size()).isEqualTo(2);
        assertThat(responses.get(0).getTicketId()).isEqualTo(ticketId3);
    }

    private void mockForCreateTicket() throws IOException {
        given(companyServiceClient.getCenterInfo(1L)).willReturn(new BaseResponseDto<>(CenterFixture.defaultCenterInfoResponseDto()));
        given(companyServiceClient.getCenterInfo(2L)).willReturn(new BaseResponseDto<>(CenterFixture.nonRefundableCenterInfoResponseDto()));
        given(amazonS3Service.saveS3Img(any(MultipartFile.class), anyString())).willReturn("test-image-key");
        given(amazonS3Service.getFileUrl("test-image-key")).willReturn("test-image-url");
    }

    private void mockForGetTicketDetail() {
        given(companyServiceClient.getOriginalCenterInfo(anyLong())).willReturn(new BaseResponseDto<>(CenterFixture.defaultOriginalCenterResponseDto()));
        given(companyServiceClient.getOriginalBusinessInfo(anyLong())).willReturn(new BaseResponseDto<>(CenterFixture.defaultOriginalBusinessResponseDto()));
    }

    private void mockForPurchaseTicket() throws IOException {
        given(companyServiceClient.getOriginalCenterInfo(anyLong())).willReturn(new BaseResponseDto<>(CenterFixture.defaultOriginalCenterResponseDto()));
    }

}