package com.example.ticketservice.service;

import com.example.ticketservice.dto.request.TicketCreateRequestDto;
import com.example.ticketservice.dto.response.AdminTicketResponseDto;
import com.example.ticketservice.dto.response.TicketCreateResponseDto;
import com.example.ticketservice.dto.response.TicketResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TicketService {
    List<TicketResponseDto> getTicketList(long centerId);

    List<AdminTicketResponseDto> getAdminTicketList(long centerId);

    TicketCreateResponseDto createTicket(long centerId, TicketCreateRequestDto requestDto, MultipartFile ticketImage);
}
