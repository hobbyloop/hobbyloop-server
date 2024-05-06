package com.example.ticketservice.dto.response;

import com.example.ticketservice.client.dto.response.CenterDistanceInfoResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryTicketResponseDto {

    private Long centerId;

    private boolean isRefundable;

    private boolean isBookmark;

    private String logoImageUrl;

    private String centerName;

    private String address;

    private int calculatedPrice;

    private float score;

    private int reviewCount;

    public static CategoryTicketResponseDto of(CenterDistanceInfoResponseDto responseDto,
                                               int calculatedPrice,
                                               float score,
                                               int reviewCount) {
        return CategoryTicketResponseDto.builder()
                .centerId(responseDto.getCenterId())
                .isRefundable(responseDto.isRefundable())
                .isBookmark(responseDto.isBookmark())
                .logoImageUrl(responseDto.getLogoImageUrl())
                .centerName(responseDto.getCenterName())
                .address(responseDto.getAddress())
                .calculatedPrice(calculatedPrice)
                .score(score)
                .reviewCount(reviewCount)
                .build();
    }
}
