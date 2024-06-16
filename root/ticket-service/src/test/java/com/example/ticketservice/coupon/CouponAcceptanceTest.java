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
        Long companyCouponId = AdminCouponSteps.createCoupon(2L, CouponFixture.companyPercentageDiscountCouponCreateRequest());

        // when
        mockForGetCenterCoupons();
        List<CouponResponseDto> responses = CouponSteps.getCenterCoupons(memberId, centerId);

        // then

    }

    private void mockForGetCenterCoupons() {
        given(companyServiceClient.getCompanyIdOfCenter(centerId)).willReturn(new BaseResponseDto<>(companyId));
    }
}
