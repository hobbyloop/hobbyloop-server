package com.example.ticketservice.service;

import com.example.ticketservice.client.dto.response.TicketClientResponseDto;
import com.example.ticketservice.client.dto.response.TicketDetailClientResponseDto;

import java.util.List;

public interface TicketClientService {
    List<TicketClientResponseDto> getTicketClientResponseDto(long centerId);

    TicketDetailClientResponseDto getTicketDetailInfo(long centerId);
}
