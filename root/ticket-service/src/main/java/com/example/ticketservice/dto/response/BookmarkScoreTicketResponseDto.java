package com.example.ticketservice.dto.response;

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

    private List<BookmarkTicketResponseDto> bookmarkTicketResponseDtoList;

    public static BookmarkScoreTicketResponseDto of(float score,
                                                    List<BookmarkTicketResponseDto> bookmarkTicketResponseDtoList) {
        return BookmarkScoreTicketResponseDto.builder()
                .score(score)
                .bookmarkTicketResponseDtoList(bookmarkTicketResponseDtoList)
                .build();
    }
}
