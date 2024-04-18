package com.example.ticketservice.service;

import com.example.ticketservice.dto.request.TicketCreateRequestDto;
import com.example.ticketservice.dto.request.TicketUpdateRequestDto;
import com.example.ticketservice.dto.response.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TicketService {
    List<TicketResponseDto> getTicketList(long centerId, long ticketId);

    List<AdminTicketResponseDto> getAdminTicketList(long centerId, long ticketId);

    TicketCreateResponseDto createTicket(long centerId, TicketCreateRequestDto requestDto, MultipartFile ticketImage);

    AdminReviewTicketResponseDto getTicketInfo(long ticketId);

    ReviewListTicketResponseDto getIOSTicketInfo(long ticketId);

    TicketDetailResponseDto getTicketDetail(long ticketId);

    TicketDetailResponseDto updateTicket(long ticketId, TicketUpdateRequestDto requestDto, MultipartFile ticketImage);

    void uploadTicket(long ticketId);

    void cancelUploadTicket(long ticketId);

    List<TicketByCenterResponseDto> getTicketListByCenter(long centerId);
}
