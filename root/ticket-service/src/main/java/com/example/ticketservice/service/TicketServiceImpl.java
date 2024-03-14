package com.example.ticketservice.service;

import com.example.ticketservice.client.CompanyServiceClient;
import com.example.ticketservice.client.dto.response.CenterInfoResponseDto;
import com.example.ticketservice.dto.BaseResponseDto;
import com.example.ticketservice.dto.response.AdminTicketResponseDto;
import com.example.ticketservice.dto.response.TicketResponseDto;
import com.example.ticketservice.entity.Ticket;
import com.example.ticketservice.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService{

    private final TicketRepository ticketRepository;

    private final CompanyServiceClient companyServiceClient;

    @Override
    @Transactional(readOnly = true)
    public List<TicketResponseDto> getTicketList(long centerId) {
        List<Ticket> ticketList = ticketRepository.findAllByCenterId(centerId);
        return ticketList.stream().map(TicketResponseDto::from).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminTicketResponseDto> getAdminTicketList(long centerId) {
        List<Ticket> ticketList = ticketRepository.findAllByCenterId(centerId);
        CenterInfoResponseDto centerInfo = companyServiceClient.getCenterInfo(centerId).getData();
        return ticketList.stream().map(t -> AdminTicketResponseDto.of(centerInfo, t)).toList();
    }
}
