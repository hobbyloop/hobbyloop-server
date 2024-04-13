package com.example.companyservice.dto.response;

import com.example.companyservice.client.dto.response.TicketInfoClientResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotCenterTicketResponseDto {

    private MainHomeCenterResponseDto centerResponseDto;

    private TicketInfoClientResponseDto ticketResponseDto;

    public static HotCenterTicketResponseDto of(MainHomeCenterResponseDto centerResponseDto,
                                                TicketInfoClientResponseDto ticketResponseDto) {
        return HotCenterTicketResponseDto.builder()
                .centerResponseDto(centerResponseDto)
                .ticketResponseDto(ticketResponseDto)
                .build();
    }
}
