package com.example.companyservice.dto.response;

import com.example.companyservice.client.dto.response.TicketClientResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CenterHomeResponseDto {

    private CenterCreateResponseDto centerInfo;

    private List<Integer> quickButtonIdList;

    private List<TicketClientResponseDto> ticketResponseDtoList;
}