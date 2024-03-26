package com.example.companyservice.client.dto.response;

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
}
