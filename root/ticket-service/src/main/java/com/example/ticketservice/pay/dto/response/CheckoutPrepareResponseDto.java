package com.example.ticketservice.pay.dto.response;

import com.example.ticketservice.coupon.dto.MemberCouponResponseDto;
import com.example.ticketservice.pay.entity.member.Checkout;
import com.example.ticketservice.ticket.entity.Ticket;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutPrepareResponseDto {
    @Schema(description = "생성된 체크아웃 아이디(이 아이디로 체크아웃 요청)", example = "1")
    private Long checkoutId;

    @Schema(description = "중복 요청 방지를 위한 멱등성 키, 3초 이내의 동일한 요청은 이미 생성된 체크아웃 데이터 활용", example = "randomString")
    private String idempotencyKey;

    @Schema(description = "할인 전 금액", example = "100000")
    private Long originalAmount;

    @Schema(description = "해당 구매에 사용 가능한 쿠폰 목록")
    private List<MemberCouponResponseDto> usableCoupons;

    @Schema(description = "회원이 보유한 총 포인트", example = "5000")
    private Long totalPoints;

    @Schema(description = "해당 구매에 최대로 사용 가능한 포인트", example = "2000")
    private Long maxUsablePoints;
    /*
    * TODO: 포인트 차감 어떻게 하지?
    * SPECIFIC_CENTER 범위인데, GENERAL 3000, SPECIFIC_CENTER 1000 있을 때
    * 1000 쓴다고 하ㅏ면 SPECIFIC_CENTER 우선으로 차감하면 되나?
    * 2000 쓴다고 하면 SPECIFIC_CENTER 1000, GENERAL 1000 이렇게?
    * */

    // 구매할 이용권/루프패스 정보
    @Schema(description = "구매할 이용권 이름", example = "2:1 필라테스 15회")
    private String ticketName;

    @Schema(description = "이용권 시설 이름", example = "하비루프 스튜디오")
    private String centerName;

    @Schema(description = "이용권 이미지 URL")
    private String ticketImageUrl;

    @Schema(description = "이용권 유효기한 - 시작일")
    private LocalDate expirationStartDate;

    @Schema(description = "이용권 유효기한 - 종료일")
    private LocalDate expirationEndDate;

    public static CheckoutPrepareResponseDto of(Checkout checkout, List<MemberCouponResponseDto> usableCoupons, Long totalPoints, Long maxUsablePoints, Ticket ticket) {
        return CheckoutPrepareResponseDto.builder()
                .checkoutId(checkout.getId())
                .idempotencyKey(checkout.getIdempotencyKey())
                .originalAmount(checkout.getOriginalAmount())
                .usableCoupons(usableCoupons)
                .totalPoints(totalPoints)
                .maxUsablePoints(maxUsablePoints)
                .ticketName(ticket.getName())
                .centerName(ticket.getCenterName())
                .ticketImageUrl(ticket.getTicketImageUrl())
                .expirationStartDate(ticket.getExpirationStartDate())
                .expirationEndDate(ticket.getExpirationEndDate())
                .build();
    }
}
