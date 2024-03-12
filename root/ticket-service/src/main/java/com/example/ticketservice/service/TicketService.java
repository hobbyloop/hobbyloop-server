package com.example.ticketservice.service;

import com.example.ticketservice.dto.response.TicketResponseDto;

import java.util.List;

public interface TicketService {
    List<TicketResponseDto> getTicketList(long centerId);
}
