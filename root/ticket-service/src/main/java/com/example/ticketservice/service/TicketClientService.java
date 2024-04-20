package com.example.ticketservice.service;

import com.example.ticketservice.client.dto.response.TicketClientResponseDto;
import com.example.ticketservice.client.dto.response.TicketDetailClientResponseDto;
import com.example.ticketservice.client.dto.response.TicketInfoClientResponseDto;
import com.example.ticketservice.dto.response.BookmarkScoreTicketResponseDto;

import java.util.List;
import java.util.Map;

public interface TicketClientService {
    List<TicketClientResponseDto> getTicketClientResponseDto(long centerId);

    Map<Long, BookmarkScoreTicketResponseDto> getBookmarkTicketList(List<Long> centerIdList);

    TicketDetailClientResponseDto getTicketDetailInfo(long centerId);

    Map<Long, TicketInfoClientResponseDto> getHotTicketList(List<Long> centerIdList);

    Map<Long, TicketInfoClientResponseDto> getRecommendTicketList(List<Long> centerIdList);
}
