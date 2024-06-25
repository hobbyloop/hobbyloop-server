package com.example.ticketservice.pay.dto.response;

import com.example.ticketservice.coupon.dto.MemberCouponResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class CheckoutPrepareResponseDto {
    private Long checkoutId;
    private Long originalAmount;
    private List<MemberCouponResponseDto> usableCoupons;
    private Long totalPoints;
    private Long maxUsablePoints;
    /*
    * 포인트 차감 어떻게 하지?
    * SPECIFIC_CENTER 범위인데, GENERAL 3000, SPECIFIC_CENTER 1000 있을 때
    * 1000 쓴다고 하ㅏ면 SPECIFIC_CENTER 우선으로 차감하면 되나?
    * 2000 쓴다고 하면 SPECIFIC_CENTER 1000, GENERAL 1000 이렇게?
    * */

    // 구매할 이용권/루프패스 정보
    private String ticketName;
    private String centerName;
    private String ticketImageUrl;
    private LocalDate expirationStartDate;
    private LocalDate expirationEndDate;
}
