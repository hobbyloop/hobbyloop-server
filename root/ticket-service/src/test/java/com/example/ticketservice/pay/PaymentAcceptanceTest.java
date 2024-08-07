package com.example.ticketservice.pay;

import com.example.ticketservice.AcceptanceTest;
import com.example.ticketservice.coupon.AdminCouponSteps;
import com.example.ticketservice.coupon.CouponSteps;
import com.example.ticketservice.coupon.dto.MemberCouponResponseDto;
import com.example.ticketservice.fixture.CenterFixture;
import com.example.ticketservice.fixture.CouponFixture;
import com.example.ticketservice.fixture.MemberFixture;
import com.example.ticketservice.fixture.TicketFixture;
import com.example.ticketservice.pay.dto.response.CheckoutPrepareResponseDto;
import com.example.ticketservice.pay.dto.response.CheckoutResponseDto;
import com.example.ticketservice.pay.dto.response.PaymentConfirmResponseDto;
import com.example.ticketservice.pay.entity.member.Payment;
import com.example.ticketservice.pay.exception.PSPConfirmationException;
import com.example.ticketservice.pay.repository.payment.PaymentRepository;
import com.example.ticketservice.pay.toss.TossPaymentClient;
import com.example.ticketservice.pay.toss.TossPaymentException;
import com.example.ticketservice.point.PointSteps;
import com.example.ticketservice.ticket.client.CompanyServiceClient;
import com.example.ticketservice.ticket.dto.BaseResponseDto;
import com.example.ticketservice.ticket.entity.Ticket;
import com.example.ticketservice.ticket.repository.ticket.TicketRepository;
import com.example.ticketservice.ticket.service.AmazonS3Service;
import com.example.ticketservice.ticket.utils.AdminTicketSteps;
import com.netflix.discovery.converters.Auto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PaymentAcceptanceTest extends AcceptanceTest {
    @MockBean
    private AmazonS3Service amazonS3Service;

    @MockBean
    CompanyServiceClient companyServiceClient;

    @MockBean
    TossPaymentClient tossPaymentClient;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    PaymentRepository paymentRepository;

    long centerId = 1L;
    long memberId = 1L;

    long ticketId;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        // Mock for create ticket
        given(companyServiceClient.getCenterInfo(centerId)).willReturn(new BaseResponseDto<>(CenterFixture.defaultCenterInfoResponseDto()));
        given(amazonS3Service.saveS3Img(any(MultipartFile.class), anyString())).willReturn("test-image-key");
        given(amazonS3Service.getFileUrl("test-image-key")).willReturn("test-image-url");

        ticketId = AdminTicketSteps.createTicket(centerId, TicketFixture.defaultTicketCreateRequest()).getTicketId();
        AdminTicketSteps.uploadTicket(ticketId);

        Long generalCouponId = AdminCouponSteps.createCoupon(1L, CouponFixture.generalPercentageDiscountCouponCreateRequest());
        CouponSteps.issueSingleCoupon(memberId, generalCouponId);

        PointSteps.earnPointWhenJoining(memberId);
    }

    @Test
    public void prepareCheckoutSuccess() throws Exception {
        // given & when
        mockForPrepareCheckout();

        CheckoutPrepareResponseDto response = PaymentSteps.prepareCheckout(memberId, ticketId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getUsableCoupons().size()).isEqualTo(1);
        assertThat(response.getMaxUsablePoints()).isEqualTo(3000L);
    }

    @Test
    public void checkoutSuccess() throws Exception {
        // given
        mockForPrepareCheckout();
        CheckoutPrepareResponseDto prepareResponse = PaymentSteps.prepareCheckout(memberId, ticketId);

        // when
        mockForCheckout();
        CheckoutResponseDto response = PaymentSteps.checkout(memberId, PaymentFixture.defaultCheckoutRequest(prepareResponse));

        // then
        assertThat(response).isNotNull();
    }

    @Test
    public void paymentConfirmationSuccess() throws Exception {
        // given
        mockForPrepareCheckout();
        mockForCheckout();
        CheckoutPrepareResponseDto prepareResponse = PaymentSteps.prepareCheckout(memberId, ticketId);
        CheckoutResponseDto checkoutResponse = PaymentSteps.checkout(memberId, PaymentFixture.defaultCheckoutRequest(prepareResponse));

        // when
        mockForConfirm(checkoutResponse);
        PaymentConfirmResponseDto response = PaymentSteps.confirm(memberId, PaymentFixture.defaultPaymentConfirmRequest(checkoutResponse));

        // then
        assertThat(response.getStatus()).isEqualTo("SUCCESS");

        // 포인트 차감, 쿠폰도 확인..
        Long points = PointSteps.getMyTotalPoints(memberId);
        assertThat(points).isNotEqualTo(3000L);
        List<MemberCouponResponseDto> coupons = CouponSteps.getAvailableMemberCoupons(memberId);
        assertThat(coupons.size()).isEqualTo(0);
    }

    @Test
    public void refundSuccessWhenTicketIssuanceFailureAfterPaymentConfirmation() throws Exception {
        // given
        mockForPrepareCheckout();
        mockForCheckout();
        CheckoutPrepareResponseDto prepareResponse = PaymentSteps.prepareCheckout(memberId, ticketId);
        CheckoutResponseDto checkoutResponse = PaymentSteps.checkout(memberId, PaymentFixture.defaultCheckoutRequest(prepareResponse));

        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow();
        ReflectionTestUtils.setField(ticket, "issueCount", ticket.getTotalCount() + 1);
        ticketRepository.save(ticket);

        mockForConfirm(checkoutResponse);
        mockForRefund(checkoutResponse);
        PaymentConfirmResponseDto response = PaymentSteps.confirm(memberId, PaymentFixture.defaultPaymentConfirmRequest(checkoutResponse));

        // then
        assertThat(response.getStatus()).isEqualTo("SUCCESS");
        Long points = PointSteps.getMyTotalPoints(memberId);
        assertThat(points).isEqualTo(3000L);
    }

    @Test
    public void paymentConfirmationFailure() throws Exception {
        // given
        mockForPrepareCheckout();
        mockForCheckout();
        CheckoutPrepareResponseDto prepareResponse = PaymentSteps.prepareCheckout(memberId, ticketId);
        CheckoutResponseDto checkoutResponse = PaymentSteps.checkout(memberId, PaymentFixture.defaultCheckoutRequest(prepareResponse));

        // when
        mockForConfirmFailure();
        PaymentConfirmResponseDto response = PaymentSteps.confirm(memberId, PaymentFixture.defaultPaymentConfirmRequest(checkoutResponse));

        // then
        assertThat(response.getStatus()).isEqualTo("FAILURE");
    }

    @Test
    public void paymentRefundSuccess() throws Exception {
        // given
        mockForPrepareCheckout();
        mockForCheckout();
        CheckoutPrepareResponseDto prepareResponse = PaymentSteps.prepareCheckout(memberId, ticketId);
        CheckoutResponseDto checkoutResponse = PaymentSteps.checkout(memberId, PaymentFixture.defaultCheckoutRequest(prepareResponse));

        mockForConfirm(checkoutResponse);
        PaymentSteps.confirm(memberId, PaymentFixture.defaultPaymentConfirmRequest(checkoutResponse));

        // when
        mockForRefund(checkoutResponse);
        PaymentConfirmResponseDto response = PaymentSteps.refund(memberId, checkoutResponse.getPaymentId());

        // then
        assertThat(response.getStatus()).isEqualTo("SUCCESS");

        Long points = PointSteps.getMyTotalPoints(memberId);
        assertThat(points).isEqualTo(3000L);
        List<MemberCouponResponseDto> coupons = CouponSteps.getAvailableMemberCoupons(memberId);
        assertThat(coupons.size()).isEqualTo(0);


    }

    private void mockForPrepareCheckout() {
        given(companyServiceClient.getCompanyIdOfCenter(centerId)).willReturn(new BaseResponseDto<>(1L));
    }

    private void mockForCheckout() {
        given(companyServiceClient.getMemberInfo(memberId)).willReturn(new BaseResponseDto<>(MemberFixture.defaultMemberInfoResponse()));
    }

    private void mockForConfirm(CheckoutResponseDto response) {
        given(tossPaymentClient.executeConfirm(any())).willReturn(Mono.just(PaymentFixture.defaultPaymentConfirmExecuteSuccessResponse(response)));
    }

    private void mockForConfirmFailure() {
        given(tossPaymentClient.executeConfirm(any())).willReturn(Mono.error(PSPConfirmationException.from(TossPaymentException.REJECT_TOSSPAY_INVALID_ACCOUNT)));
    }

    private void mockForRefund(CheckoutResponseDto response) {
        given(tossPaymentClient.executeFullCancel(any(), any(), any())).willReturn(Mono.just(PaymentFixture.defaultPaymentRefundExecuteSuccessResponse(response)));
    }
}
