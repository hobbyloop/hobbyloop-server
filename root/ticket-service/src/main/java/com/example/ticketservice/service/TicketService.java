package com.example.ticketservice.service;

import com.example.ticketservice.dto.request.TicketCreateRequestDto;
import com.example.ticketservice.dto.response.AdminTicketResponseDto;
import com.example.ticketservice.dto.response.BookmarkTicketResponseDto;
import com.example.ticketservice.dto.response.TicketCreateResponseDto;
import com.example.ticketservice.dto.response.TicketResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface TicketService {
    List<TicketResponseDto> getTicketList(long centerId);

    List<AdminTicketResponseDto> getAdminTicketList(long centerId, long ticketId);

    TicketCreateResponseDto createTicket(long centerId, TicketCreateRequestDto requestDto, MultipartFile ticketImage);

    Map<Long, List<BookmarkTicketResponseDto>> getBookmarkTicketList(List<Long> centerIdList);
}
