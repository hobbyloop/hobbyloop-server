package com.example.ticketservice.service;

import com.example.ticketservice.client.dto.response.TicketClientResponseDto;
import com.example.ticketservice.client.dto.response.TicketDetailClientResponseDto;
import com.example.ticketservice.entity.Ticket;
import com.example.ticketservice.repository.ticket.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketClientServiceImpl implements TicketClientService {

    private final TicketRepository ticketRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TicketClientResponseDto> getTicketClientResponseDto(long centerId) {
        List<Ticket> ticketList = ticketRepository.findAllByCenterId(centerId);
        return ticketList.stream().map(TicketClientResponseDto::from).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TicketDetailClientResponseDto getTicketDetailInfo(long centerId) {
        return null;
    }
}
