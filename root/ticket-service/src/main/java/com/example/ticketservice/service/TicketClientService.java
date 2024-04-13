package com.example.ticketservice.service;

import com.example.ticketservice.client.dto.response.TicketClientResponseDto;
import com.example.ticketservice.client.dto.response.TicketDetailClientResponseDto;
import com.example.ticketservice.client.dto.response.TicketInfoClientResponseDto;

import java.util.List;
import java.util.Map;

public interface TicketClientService {
    List<TicketClientResponseDto> getTicketClientResponseDto(long centerId);

    TicketDetailClientResponseDto getTicketDetailInfo(long centerId);

    Map<Long, TicketInfoClientResponseDto> getHotTicketList(List<Long> centerIdList);

    Map<Long, TicketInfoClientResponseDto> getRecommendTicketList(List<Long> centerIdList);
}
