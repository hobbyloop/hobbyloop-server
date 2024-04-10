package com.example.companyservice.dto.response;

import com.example.companyservice.entity.Center;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RelatedCenterResponseDto {

    private String centerName;

    private String address;

    private String category;

    private String logoImageUrl;

    private float score;

    private int reviewCount;

    private boolean isBookmark;

    private int minPrice;

    private List<String> tagList;

    public static RelatedCenterResponseDto of(Center center,
                                              float score,
                                              int reviewCount,
                                              boolean isBookmark,
                                              int minPrice,
                                              List<String> tagList) {
        return RelatedCenterResponseDto.builder()
                .centerName(center.getCenterName())
                .address(center.getAddress())
                .category("")
                .logoImageUrl(center.getLogoImageUrl())
                .score(score)
                .reviewCount(reviewCount)
                .isBookmark(isBookmark)
                .minPrice(minPrice)
                .tagList(tagList)
                .build();
    }
}
