package com.example.companyservice.company.dto.response;

import com.example.companyservice.company.client.dto.response.BookmarkTicketResponseDto;
import com.example.companyservice.company.entity.Bookmark;
import com.example.companyservice.company.entity.Center;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookmarkCenterResponseDto {

    private long bookmarkId;

    private long centerId;

    private String centerName;

    private boolean isRefundable;

    private String address;

    private float score;

    private int reviewCount;

    private String logoImageUrl;

    private List<BookmarkTicketResponseDto> ticketList;

    public static BookmarkCenterResponseDto of(BookmarkCenterDto dto,
                                               float score,
                                               int reviewCount,
                                               List<BookmarkTicketResponseDto> ticketList) {
        return BookmarkCenterResponseDto.builder()
                .bookmarkId(dto.getBookmarkId())
                .centerId(dto.getCenterId())
                .centerName(dto.getCenterName())
                .isRefundable(dto.isRefundable())
                .address(dto.getAddress())
                .score(score)
                .reviewCount(reviewCount)
                .logoImageUrl(dto.getLogoImageUrl())
                .ticketList(ticketList)
                .build();
    }
}
