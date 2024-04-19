package com.example.companyservice.company.client.dto.response;

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
}
