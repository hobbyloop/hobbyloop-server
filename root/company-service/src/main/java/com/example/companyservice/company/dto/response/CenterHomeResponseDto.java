package com.example.companyservice.company.dto.response;

import com.example.companyservice.company.client.dto.response.TicketClientBaseResponseDto;
import com.example.companyservice.company.client.dto.response.TicketClientResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CenterHomeResponseDto {

    private CenterCreateResponseDto centerInfo;

    private List<Integer> quickButtonIdList;

    private int membershipCount;

    private List<TicketClientResponseDto> ticketResponseDtoList;

    public static CenterHomeResponseDto of(CenterCreateResponseDto centerInfo,
                                           List<Integer> quickButtonIdList,
                                           TicketClientBaseResponseDto ticketBaseResponseDto) {
        return CenterHomeResponseDto.builder()
                .centerInfo(centerInfo)
                .quickButtonIdList(quickButtonIdList)
                .membershipCount(ticketBaseResponseDto.getMembershipCount())
                .ticketResponseDtoList(ticketBaseResponseDto.getTicketClientResponseDtoList())
                .build();
    }
}