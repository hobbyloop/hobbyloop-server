package com.example.ticketservice.service;

import com.example.ticketservice.client.CompanyServiceClient;
import com.example.ticketservice.client.MemberServiceClient;
import com.example.ticketservice.client.dto.response.CenterInfoResponseDto;
import com.example.ticketservice.client.dto.response.MemberInfoResponseDto;
import com.example.ticketservice.common.exception.ApiException;
import com.example.ticketservice.common.exception.ExceptionEnum;
import com.example.ticketservice.dto.request.CenterMembershipJoinRequestDto;
import com.example.ticketservice.dto.response.CenterMemberResponseDto;
import com.example.ticketservice.dto.response.CenterMembershipDetailResponseDto;
import com.example.ticketservice.dto.response.CenterMembershipJoinedResponseDto;
import com.example.ticketservice.dto.response.TicketResponseDto;
import com.example.ticketservice.entity.CenterMembership;
import com.example.ticketservice.entity.CenterMembershipStatusEnum;
import com.example.ticketservice.entity.Ticket;
import com.example.ticketservice.entity.UserTicket;
import com.example.ticketservice.event.CenterMemberJoinedEvent;
import com.example.ticketservice.repository.centermembership.CenterMembershipRepository;
import com.example.ticketservice.repository.ticket.TicketRepository;
import com.example.ticketservice.repository.ticket.UserTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CenterMembershipServiceImpl implements CenterMembershipService {
    private final CenterMembershipRepository centerMembershipRepository;
    private final UserTicketRepository userTicketRepository;
    private final TicketRepository ticketRepository;
    private final MemberServiceClient memberServiceClient;
    private final CompanyServiceClient companyServiceClient;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void joinCenterMembership(Long centerId, Long memberId) {
        Optional<CenterMembership> existingCenterMembership = centerMembershipRepository.findById(centerId);
        if (existingCenterMembership.isPresent()) {
            CenterMembership centerMembership = existingCenterMembership.get();
            if (centerMembership.isExpired() || centerMembership.isExpiringSoon()) {
                centerMembership.renew();
                return;
            }
            throw new ApiException(ExceptionEnum.CENTER_MEMBERSHIP_ALREADY_JOINED_EXCEPTION);
        }

        MemberInfoResponseDto memberInfo = memberServiceClient.getMemberInfo(memberId).getData();

        CenterMembership centerMembership = CenterMembership.builder()
                .centerId(centerId)
                .memberId(memberId)
                .memberName(memberInfo.getMemberName())
                .phoneNumber(memberInfo.getPhoneNumber())
                .status(CenterMembershipStatusEnum.ACTIVE.getStatusType())
                .build();

        centerMembershipRepository.save(centerMembership);
    }

    @Override
    @Transactional
    public CenterMembershipJoinedResponseDto joinCenterMembershipByAdmin(
            Long centerId, Long memberId, CenterMembershipJoinRequestDto request) {
        if (centerMembershipRepository.existsByMemberId(memberId)) {
            throw new ApiException(ExceptionEnum.CENTER_MEMBERSHIP_ALREADY_JOINED_EXCEPTION);
        }

        CenterMembership centerMembership = CenterMembership.builder()
                .centerId(centerId)
                .memberId(memberId)
                .memberName(request.getMemberName())
                .phoneNumber(request.getPhoneNumber())
                .status(CenterMembershipStatusEnum.ACTIVE.getStatusType())
                .build();

        centerMembershipRepository.save(centerMembership);

        eventPublisher.publishEvent(new CenterMemberJoinedEvent(memberId, request.getTicketId()));

        Ticket ticket = ticketRepository.findById(request.getTicketId())
                .orElseThrow(() -> new ApiException(ExceptionEnum.TICKET_NOT_EXIST_EXCEPTION));

        return CenterMembershipJoinedResponseDto.of(request, ticket.getName());
    }

    @Override
    @Transactional
    public CenterMembershipDetailResponseDto getCenterMembershipDetail(long centerMembershipId) {
        CenterMembership centerMembership = centerMembershipRepository.findById(centerMembershipId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.CENTER_MEMBERSHIP_NOT_EXIST_EXCEPTION));
        MemberInfoResponseDto memberInfo = memberServiceClient.getMemberInfo(centerMembershipId).getData();

        centerMembership.updateMemberInfo(memberInfo.getMemberName(), memberInfo.getPhoneNumber());

        List<UserTicket> userTicketList = userTicketRepository.findAllByMemberId(centerMembership.getMemberId());
        List<TicketResponseDto> ticketList = userTicketList.stream()
                .map(UserTicket::getTicket)
                .map(ticket -> {
                    CenterInfoResponseDto centerInfo = companyServiceClient.getCenterInfo(ticket.getCenterId()).getData();
                    return TicketResponseDto.from(ticket, centerInfo);
                })
                .toList();

        return CenterMembershipDetailResponseDto.of(centerMembershipId, memberInfo, ticketList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CenterMemberResponseDto> getCenterMemberList(Long centerId, int pageNo, int sortId) {
        List<CenterMembership> centerMembershipList = centerMembershipRepository.getCenterMembershipListByCenterId(centerId, pageNo, sortId);

        return centerMembershipList.stream()
                .map(c -> {
                    UserTicket userTicket = userTicketRepository.findFirstByMemberIdOrderByCreatedAtDesc(c.getMemberId())
                            .orElseThrow();
                    return CenterMemberResponseDto.of(c, userTicket.getTicket());
                })
                .toList();
    }
}
