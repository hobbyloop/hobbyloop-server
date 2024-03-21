package com.example.companyservice.dto.response;

import com.example.companyservice.client.dto.response.BookmarkTicketResponseDto;
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

    private String centerName;

    private String address;

    private float score;

    private String logoImageUrl;

    private List<BookmarkTicketResponseDto> ticketList;

    public static BookmarkCenterResponseDto of(Center center, List<BookmarkTicketResponseDto> ticketList) {
        return BookmarkCenterResponseDto.builder()
                .centerName(center.getCenterName())
                .address(center.getAddress())
                .score(center.getScore())
                .logoImageUrl(center.getLogoImageUrl())
                .ticketList(ticketList)
                .build();
    }
}
