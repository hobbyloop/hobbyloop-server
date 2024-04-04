package com.example.ticketservice.service;

import com.example.ticketservice.dto.request.TicketCreateRequestDto;
import com.example.ticketservice.dto.response.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface TicketService {
    List<TicketResponseDto> getTicketList(long centerId);

    List<AdminTicketResponseDto> getAdminTicketList(long centerId, long ticketId);

    TicketCreateResponseDto createTicket(long centerId, TicketCreateRequestDto requestDto, MultipartFile ticketImage);

    Map<Long, BookmarkScoreTicketResponseDto> getBookmarkTicketList(List<Long> centerIdList);

    AdminReviewTicketResponseDto getTicketInfo(long ticketId);

    ReviewListTicketResponseDto getIOSTicketInfo(long ticketId);

    TicketDetailResponseDto getTicketDetail(long ticketId);
}
