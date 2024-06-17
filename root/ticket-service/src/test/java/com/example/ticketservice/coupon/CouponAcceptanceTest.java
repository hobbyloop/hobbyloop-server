package com.example.ticketservice.coupon;

import com.example.ticketservice.AcceptanceTest;
import com.example.ticketservice.coupon.dto.CouponResponseDto;
import com.example.ticketservice.fixture.CouponFixture;
import com.example.ticketservice.ticket.client.CompanyServiceClient;
import com.example.ticketservice.ticket.dto.BaseResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

public class CouponAcceptanceTest extends AcceptanceTest {

    @MockBean
    private CompanyServiceClient companyServiceClient;

    long companyId = 1L;
    long centerId = 1L;
    long memberId = 1L;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void createCouponByAdminSuccess() throws Exception {
        // given & when
        Long couponId = AdminCouponSteps.createCoupon(1L, CouponFixture.generalPercentageDiscountCouponCreateRequest());

        // then
        assertThat(couponId).isNotNull();
    }

    @Test
    public void getCenterCouponsSuccess() throws Exception {
        // given
        Long generalCouponId = AdminCouponSteps.createCoupon(1L, CouponFixture.generalPercentageDiscountCouponCreateRequest());
        Long outOfDateCouponId = AdminCouponSteps.createCoupon(1L, CouponFixture.generalOutOfDateCouponCreateRequest());
        Long companyCouponId = AdminCouponSteps.createCoupon(2L, CouponFixture.companyPercentageDiscountCouponCreateRequest(companyId));

        // when
        mockForGetCenterCoupons();
        List<CouponResponseDto> responses = CouponSteps.getCenterCoupons(memberId, centerId);

        // then
        assertThat(responses).isNotNull();
        assertThat(responses.size()).isEqualTo(2);
    }

    @Test
    public void issueSingleCouponSuccess() throws Exception {
        // given
        Long centerCouponId = AdminCouponSteps.createCoupon(2L, CouponFixture.centerPercentageDiscountCouponCreateRequest(centerId));

        // when
        Long issuedCouponId = CouponSteps.issueSingleCoupon(memberId, centerCouponId);

        // then
        assertThat(issuedCouponId).isNotNull();
    }

    @Test
    public void getMyCouponCountSuccess() throws Exception {
        // given
        Long centerCouponId = AdminCouponSteps.createCoupon(2L, CouponFixture.centerPercentageDiscountCouponCreateRequest(centerId));
        CouponSteps.issueSingleCoupon(memberId, centerCouponId);

        // when
        Long couponCount = CouponSteps.getMyCouponCount(memberId);

        // then
        assertThat(couponCount).isNotNull();
        assertThat(couponCount).isEqualTo(1);
    }

    private void mockForGetCenterCoupons() {
        given(companyServiceClient.getCompanyIdOfCenter(centerId)).willReturn(new BaseResponseDto<>(companyId));
    }
}
