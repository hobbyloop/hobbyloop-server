package com.example.companyservice.dto.response;

import com.example.companyservice.client.dto.response.HotTicketResponseDto;
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

    private HotTicketResponseDto ticketResponseDto;

    public static RecommendedCenterResponseDto of(MainHomeCenterResponseDto centerResponseDto,
                                                  HotTicketResponseDto ticketResponseDto) {
        return RecommendedCenterResponseDto.builder()
                .centerResponseDto(centerResponseDto)
                .ticketResponseDto(ticketResponseDto)
                .build();
    }
}
