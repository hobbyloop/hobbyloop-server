package com.example.ticketservice.pay;

import com.example.ticketservice.AcceptanceTest;
import com.example.ticketservice.coupon.AdminCouponSteps;
import com.example.ticketservice.coupon.CouponSteps;
import com.example.ticketservice.fixture.CenterFixture;
import com.example.ticketservice.fixture.CouponFixture;
import com.example.ticketservice.fixture.TicketFixture;
import com.example.ticketservice.pay.dto.response.CheckoutPrepareResponseDto;
import com.example.ticketservice.pay.dto.response.CheckoutResponseDto;
import com.example.ticketservice.pay.toss.TossPaymentClient;
import com.example.ticketservice.point.PointSteps;
import com.example.ticketservice.ticket.client.CompanyServiceClient;
import com.example.ticketservice.ticket.dto.BaseResponseDto;
import com.example.ticketservice.ticket.service.AmazonS3Service;
import com.example.ticketservice.ticket.utils.AdminTicketSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.multipart.MultipartFile;

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
        CheckoutResponseDto response = PaymentSteps.checkout(memberId, PaymentFixture.defaultCheckoutRequest(prepareResponse));

        // then
        assertThat(response).isNotNull();
    }

    private void mockForPrepareCheckout() {
        given(companyServiceClient.getCompanyIdOfCenter(centerId)).willReturn(new BaseResponseDto<>(1L));
    }

    private void mockForConfirm() {
        //given(tossPaymentClient.executeConfirm())
    }
}
