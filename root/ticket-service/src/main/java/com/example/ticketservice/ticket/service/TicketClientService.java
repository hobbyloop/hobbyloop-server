package com.example.ticketservice.ticket.service;

import com.example.ticketservice.ticket.client.dto.response.TicketClientBaseResponseDto;
import com.example.ticketservice.ticket.client.dto.response.TicketClientForLectureResponseDto;
import com.example.ticketservice.ticket.client.dto.response.TicketDetailClientResponseDto;
import com.example.ticketservice.ticket.client.dto.response.TicketInfoClientResponseDto;
import com.example.ticketservice.ticket.dto.response.BookmarkScoreTicketResponseDto;

import java.util.List;
import java.util.Map;

public interface TicketClientService {
    TicketClientBaseResponseDto getTicketList(long centerId);

    Map<Long, BookmarkScoreTicketResponseDto> getBookmarkTicketList(List<Long> centerIdList);

    TicketDetailClientResponseDto getTicketDetailInfo(long centerId);

    Map<Long, TicketInfoClientResponseDto> getHotTicketList(List<Long> centerIdList);

    Map<Long, TicketInfoClientResponseDto> getRecommendTicketList(List<Long> centerIdList);

    boolean getHasTicket(long centerId);

}
