package com.example.ticketservice.ticket.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookmarkScoreTicketResponseDto {

    private float score;

    private int reviewCount;

    private List<BookmarkTicketResponseDto> bookmarkTicketResponseDtoList;

    public static BookmarkScoreTicketResponseDto of(float score,
                                                    int reviewCount,
                                                    List<BookmarkTicketResponseDto> bookmarkTicketResponseDtoList) {
        return BookmarkScoreTicketResponseDto.builder()
                .score(score)
                .reviewCount(reviewCount)
                .bookmarkTicketResponseDtoList(bookmarkTicketResponseDtoList)
                .build();
    }
}
