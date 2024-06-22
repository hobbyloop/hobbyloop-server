package com.example.companyservice.company.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IsBookmarkResponseDto {

    private boolean isBookmark;

    public static IsBookmarkResponseDto of(boolean isBookmark) {
        return IsBookmarkResponseDto.builder()
                .isBookmark(isBookmark)
                .build();
    }
}
