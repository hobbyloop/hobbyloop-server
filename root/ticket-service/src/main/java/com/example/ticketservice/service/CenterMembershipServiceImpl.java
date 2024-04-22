package com.example.ticketservice.service;

import com.example.ticketservice.client.CompanyServiceClient;
import com.example.ticketservice.client.MemberServiceClient;
import com.example.ticketservice.client.dto.response.CenterInfoResponseDto;
import com.example.ticketservice.client.dto.response.MemberInfoResponseDto;
import com.example.ticketservice.dto.response.CenterMemberResponseDto;
import com.example.ticketservice.dto.response.CenterMembershipDetailResponseDto;
import com.example.ticketservice.dto.response.TicketResponseDto;
import com.example.ticketservice.entity.CenterMembership;
import com.example.ticketservice.entity.CenterMembershipStatusEnum;
import com.example.ticketservice.entity.UserTicket;
import com.example.ticketservice.repository.centermembership.CenterMembershipRepository;
import com.example.ticketservice.repository.ticket.UserTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CenterMembershipServiceImpl implements CenterMembershipService {
    private final CenterMembershipRepository centerMembershipRepository;
    private final UserTicketRepository userTicketRepository;
    private final MemberServiceClient memberServiceClient;
    private final CompanyServiceClient companyServiceClient;

    @Override
    @Transactional
    public void joinCenterMembership(Long centerId, Long memberId) {
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
    public CenterMembershipDetailResponseDto getCenterMembershipDetail(long centerMembershipId) {
        CenterMembership centerMembership = centerMembershipRepository.findById(centerMembershipId).orElseThrow();
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
