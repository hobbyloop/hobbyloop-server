package com.example.ticketservice.ticket.service;

import com.example.ticketservice.ticket.client.CompanyServiceClient;
import com.example.ticketservice.ticket.client.MemberServiceClient;
import com.example.ticketservice.ticket.client.dto.response.CenterInfoResponseDto;
import com.example.ticketservice.ticket.client.dto.response.MemberInfoResponseDto;
import com.example.ticketservice.ticket.client.dto.response.OriginalCenterResponseDto;
import com.example.ticketservice.ticket.common.exception.ApiException;
import com.example.ticketservice.ticket.common.exception.ExceptionEnum;
import com.example.ticketservice.ticket.dto.response.*;
import com.example.ticketservice.ticket.entity.LectureReservation;
import com.example.ticketservice.ticket.entity.Ticket;
import com.example.ticketservice.ticket.entity.UserTicket;
import com.example.ticketservice.ticket.event.UserTicketApprovedEvent;
import com.example.ticketservice.pay.entity.PurchaseHistory;
import com.example.ticketservice.pay.repository.purchasehistory.PurchaseHistoryRepository;
import com.example.ticketservice.ticket.repository.reservation.LectureReservationRepository;
import com.example.ticketservice.ticket.repository.ticket.TicketRepository;
import com.example.ticketservice.ticket.repository.ticket.UserTicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
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
    private final LectureReservationRepository lectureReservationRepository;

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
    public List<AvailableUserTicketsWithCenterInfo> getAvailableUserTicketList(long memberId) {
        List<UserTicket> userTicketList = userTicketRepository.findAvailableUserTicketList(memberId);

        Map<Long, AvailableUserTicketsWithCenterInfo> centerInfoMap = new HashMap<>();

        for (UserTicket userTicket : userTicketList) {
            Long centerId = userTicket.getTicket().getCenterId();

            if (!centerInfoMap.containsKey(centerId)) {
                CenterInfoResponseDto centerInfo = companyServiceClient.getCenterInfo(userTicket.getTicket().getCenterId()).getData();
                AvailableUserTicketsWithCenterInfo availableUserTicketsWithCenterInfo = AvailableUserTicketsWithCenterInfo.of(centerId, centerInfo.getCenterName(), centerInfo.isRefundable(), new ArrayList<>());
                centerInfoMap.put(centerId, availableUserTicketsWithCenterInfo);
            }

            AvailableUserTicketsWithCenterInfo availableUserTicketsWithCenterInfo = centerInfoMap.get(centerId);

            AvailableUserTicketResponseDto availableUserTicket = AvailableUserTicketResponseDto.of(userTicket);

            availableUserTicketsWithCenterInfo.getAvailableUserTickets().add(availableUserTicket);
        }

        return new ArrayList<>(centerInfoMap.values());
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

    @Override
    @Transactional(readOnly = true)
    public List<UserTicketUsingHistoryResponseDto> getUserTicketUsingHistory(long memberId) {
        List<UserTicket> userTicketList = userTicketRepository.findAllByMemberIdOrderByCreatedAtDesc(memberId);

        List<UserTicketUsingHistoryResponseDto> result = new ArrayList<>();

        for (UserTicket userTicket : userTicketList) {
            CenterInfoResponseDto centerInfo = companyServiceClient.getCenterInfo(userTicket.getTicket().getCenterId()).getData();

            Map<String, List<UsingHistoryDto>> usingHistoriesByMonth = new HashMap<>();
            List<LectureReservation> reservations = lectureReservationRepository.findByUserTicket(userTicket);
            for (LectureReservation reservation : reservations) {
                String yearMonth = reservation.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM"));

                if (!usingHistoriesByMonth.containsKey(yearMonth)) {
                    usingHistoriesByMonth.put(yearMonth, new ArrayList<>());
                }

                usingHistoriesByMonth.get(yearMonth).add(UsingHistoryDto.from(reservation));
            }

            List<UsingHistoryByMonthDto> usingHistories = new ArrayList<>();
            for (Map.Entry<String, List<UsingHistoryDto>> entry : usingHistoriesByMonth.entrySet()) {
                List<UsingHistoryDto> sortedUsingHistories = entry.getValue().stream()
                                .sorted(Comparator.comparing(UsingHistoryDto::getUsedAt).reversed())
                                        .toList();
                usingHistories.add(new UsingHistoryByMonthDto(entry.getKey(), sortedUsingHistories));
            }

            usingHistories.sort(Comparator.comparing((UsingHistoryByMonthDto history) -> YearMonth.parse(history.getYearMonth(), DateTimeFormatter.ofPattern("yyyy/MM"))).reversed());

            UserTicketUsingHistoryResponseDto dto = UserTicketUsingHistoryResponseDto.of(userTicket, centerInfo.getCenterName(), usingHistories);

            result.add(dto);
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserTicketExpiringHistoryResponseDto> getUserTicketExpiringHistory(long memberId) {
        List<UserTicket> userTicketList = userTicketRepository.findAllByMemberIdOrderByCreatedAtDesc(memberId);

        List<UserTicketExpiringHistoryResponseDto> result = new ArrayList<>();
        for (UserTicket userTicket : userTicketList) {
            if (LocalDate.now().isBefore(userTicket.getEndDate())) {
                continue;
            }

            CenterInfoResponseDto centerInfo = companyServiceClient.getCenterInfo(userTicket.getTicket().getCenterId()).getData();

            String yearMonth = userTicket.getEndDate().format(DateTimeFormatter.ofPattern("yyyy/MM"));

            UserTicketExpiringHistoryResponseDto dto = UserTicketExpiringHistoryResponseDto.of(userTicket, centerInfo.getCenterName(), yearMonth);

            result.add(dto);
        }
        return result;
    }
}
