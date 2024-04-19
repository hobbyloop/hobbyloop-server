package com.example.companyservice.company.dto.response;

import com.example.companyservice.company.client.dto.response.TicketInfoClientResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendedCenterResponseDto {

    private MainHomeCenterResponseDto centerResponseDto;

    private TicketInfoClientResponseDto ticketResponseDto;

    public static RecommendedCenterResponseDto of(MainHomeCenterResponseDto centerResponseDto,
                                                  TicketInfoClientResponseDto ticketResponseDto) {
        return RecommendedCenterResponseDto.builder()
                .centerResponseDto(centerResponseDto)
                .ticketResponseDto(ticketResponseDto)
                .build();
    }
}
