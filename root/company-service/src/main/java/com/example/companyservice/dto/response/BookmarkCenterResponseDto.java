package com.example.companyservice.dto.response;

import com.example.companyservice.client.dto.response.BookmarkTicketResponseDto;
import com.example.companyservice.entity.Bookmark;
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
public class BookmarkCenterResponseDto {

    private long bookmarkId;

    private long centerId;

    private String centerName;

    private String address;

    private float score;

    private String logoImageUrl;

    private List<BookmarkTicketResponseDto> ticketList;

    public static BookmarkCenterResponseDto of(Bookmark bookmark,
                                               Center center,
                                               float score,
                                               List<BookmarkTicketResponseDto> ticketList) {
        return BookmarkCenterResponseDto.builder()
                .bookmarkId(bookmark.getId())
                .centerId(center.getId())
                .centerName(center.getCenterName())
                .address(center.getAddress())
                .score(score)
                .logoImageUrl(center.getLogoImageUrl())
                .ticketList(ticketList)
                .build();
    }
}
