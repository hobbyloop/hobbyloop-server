package com.example.ticketservice.service;

import com.example.ticketservice.client.CompanyServiceClient;
import com.example.ticketservice.client.MemberServiceClient;
import com.example.ticketservice.client.dto.response.CenterInfoResponseDto;
import com.example.ticketservice.client.dto.response.MemberInfoResponseDto;
import com.example.ticketservice.client.dto.response.OriginalCenterResponseDto;
import com.example.ticketservice.common.exception.ApiException;
import com.example.ticketservice.common.exception.ExceptionEnum;
import com.example.ticketservice.dto.response.*;
import com.example.ticketservice.entity.Ticket;
import com.example.ticketservice.entity.UserTicket;
import com.example.ticketservice.event.UserTicketApprovedEvent;
import com.example.ticketservice.pay.entity.PurchaseHistory;
import com.example.ticketservice.pay.repository.purchasehistory.PurchaseHistoryRepository;
import com.example.ticketservice.repository.ticket.TicketRepository;
import com.example.ticketservice.repository.ticket.UserTicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserTicketServiceImpl implements UserTicketService {
    private final UserTicketRepository userTicketRepository;
    private final TicketRepository ticketRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final CompanyServiceClient companyServiceClient;
    private final MemberServiceClient memberServiceClient;
    private final PurchaseHistoryRepository purchaseHistoryRepository;

    @Override
    @Transactional
    public Long purchaseTicket(long memberId, long ticketId) {
        Ticket ticket = ticketRepository.findForUpdate(ticketId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.TICKET_NOT_EXIST_EXCEPTION));

        ticket.checkCanPurchase();
        // TODO: 쿠폰, 포인트 적용
        // TODO: 결제 -> PayClient.pay(ticket.getPrice());
        UserTicket userTicket = UserTicket.of(ticket, memberId);
        userTicketRepository.save(userTicket);

        // API Test를 위한 구매기록 저장 로직 언제든지 수정하셔도 됩니다.
        OriginalCenterResponseDto centerInfo = companyServiceClient.getOriginalCenterInfo(ticket.getCenterId()).getData();
        PurchaseHistory purchaseHistory = PurchaseHistory.of(centerInfo, memberId, ticket, null);
        purchaseHistoryRepository.save(purchaseHistory);

        return userTicket.getId();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processOfflinePurchaseTicket(long memberId, long ticketId) {
        Ticket ticket = ticketRepository.findForUpdate(ticketId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.TICKET_NOT_EXIST_EXCEPTION));

        UserTicket userTicket = UserTicket.of(ticket, memberId);
        userTicket.approve();
        userTicketRepository.save(userTicket);

        ticket.issue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UnapprovedUserTicketListResponseDto> getUnapprovedUserTicketList(long centerId) {
        List<Long> ticketIds = ticketRepository.findAllByCenterId(centerId).stream().map(Ticket::getId).toList();
        List<UserTicket> userTicketList = ticketIds.stream()
                .flatMap(ticketId -> userTicketRepository.findAllByTicketIdAndIsApproveFalse(ticketId).stream())
                .collect(Collectors.toList());
        return userTicketList.stream()
                .map(userTicket -> {
                    MemberInfoResponseDto memberInfo = memberServiceClient.getMemberInfo(userTicket.getMemberId()).getData();
                    return UnapprovedUserTicketListResponseDto.of(userTicket, memberInfo);
                })
                .sorted(Comparator.comparing(UnapprovedUserTicketListResponseDto::getCreatedAt).reversed())
                .toList();
    }

    @Override
    @Transactional
    public void approveUserTicket(long userTicketId) {
        UserTicket userTicket = userTicketRepository.findById(userTicketId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.USER_TICKET_NOT_EXIST_EXCEPTION));
        Ticket ticket = userTicket.getTicket();

        userTicket.approve();
        ticket.issue();

        eventPublisher.publishEvent(new UserTicketApprovedEvent(ticket.getCenterId(), userTicket.getMemberId()));
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, AvailableUserTicketsWithCenterInfo> getAvailableUserTicketList(long memberId) {
        List<UserTicket> userTicketList = userTicketRepository.findAvailableUserTicketList(memberId);

        return userTicketList.stream()
                .collect(Collectors.groupingBy(
                        userTicket -> {
                            Long centerId = userTicket.getTicket().getCenterId();
                            CenterInfoResponseDto centerInfoResponseDto = companyServiceClient.getCenterInfo(centerId).getData();
                            return centerInfoResponseDto.getCenterName();
                        },
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    UserTicket firstUserTicket = list.get(0);
                                    Long centerId = firstUserTicket.getTicket().getCenterId();
                                    CenterInfoResponseDto centerInfoResponseDto = companyServiceClient.getCenterInfo(centerId).getData();
                                    List<AvailableUserTicketResponseDto> userTicketResponseDtoList = list.stream()
                                            .map(AvailableUserTicketResponseDto::of)
                                            .collect(Collectors.toList());
                                    return AvailableUserTicketsWithCenterInfo.of(centerId, centerInfoResponseDto.isRefundable(), userTicketResponseDtoList);
                                }
                        )
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public Map<YearMonth, List<RecentPurchaseUserTicketListResponseDto>> getRecentPurchaseUserTicketList(long memberId) {
        List<UserTicket> userTicketList = userTicketRepository.findAllByMemberId(memberId);
        return userTicketList.stream()
                .collect(Collectors.groupingBy(
                        userTicket -> YearMonth.from(userTicket.getCreatedAt()),
                        TreeMap::new,
                        Collectors.collectingAndThen(
                                Collectors.mapping(
                                        userTicket -> {
                                            CenterInfoResponseDto centerInfo = companyServiceClient.getCenterInfo(userTicket.getTicket().getCenterId()).getData();
                                            return RecentPurchaseUserTicketListResponseDto.of(userTicket, centerInfo.getCenterName());
                                        },
                                        Collectors.toCollection(ArrayList::new)
                                ),
                                list -> {
                                    list.sort(Comparator.comparing(RecentPurchaseUserTicketListResponseDto::getCreatedAt));
                                    return list;
                                }
                        )
                ));
    }
}
