package com.example.ticketservice.ticket.service;

import com.example.ticketservice.ticket.client.dto.response.*;
import com.example.ticketservice.ticket.dto.response.BookmarkScoreTicketResponseDto;
import com.example.ticketservice.ticket.dto.response.BookmarkTicketResponseDto;
import com.example.ticketservice.ticket.entity.Review;
import com.example.ticketservice.ticket.entity.Ticket;
import com.example.ticketservice.pay.dto.request.PurchaseHistoryInOneWeekResponseDto;
import com.example.ticketservice.pay.repository.purchasehistory.PurchaseHistoryRepository;
import com.example.ticketservice.ticket.repository.centermembership.CenterMembershipRepository;
import com.example.ticketservice.ticket.repository.review.ReviewRepository;
import com.example.ticketservice.ticket.repository.ticket.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketClientServiceImpl implements TicketClientService {

    private final TicketRepository ticketRepository;

    private final ReviewRepository reviewRepository;

    private final PurchaseHistoryRepository purchaseHistoryRepository;

    private final CenterMembershipRepository centerMembershipRepository;

    @Override
    @Transactional(readOnly = true)
    public TicketClientBaseResponseDto getTicketList(long centerId) {
        List<Ticket> ticketList = ticketRepository.findAllByCenterId(centerId);
        List<TicketClientResponseDto> ticketClientResponseDtoList = ticketList.stream().map(TicketClientResponseDto::from).toList();
        int membershipCount = centerMembershipRepository.countByCenterId(centerId);
        return TicketClientBaseResponseDto.of(membershipCount, ticketClientResponseDtoList);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, BookmarkScoreTicketResponseDto> getBookmarkTicketList(List<Long> centerIdList) {
        Map<Long, BookmarkScoreTicketResponseDto> bookmarkTicketResponseDtoMap = new HashMap<>();
        centerIdList.forEach((i) -> {
            List<Review> reviewList = reviewRepository.findAllByCenterId(i);
            float score = getScore(reviewList);
            List<Ticket> ticketList = ticketRepository.findAllByCenterIdOrderByCalculatedPrice(i);
            List<BookmarkTicketResponseDto> bookmarkTicketResponseDtoList = ticketList.stream().map(BookmarkTicketResponseDto::from).toList();
            BookmarkScoreTicketResponseDto bookmarkScoreTicketResponseDto = BookmarkScoreTicketResponseDto.of(score, reviewList.size(), bookmarkTicketResponseDtoList);
            bookmarkTicketResponseDtoMap.put(i, bookmarkScoreTicketResponseDto);
        });
        return bookmarkTicketResponseDtoMap;
    }

    @Override
    @Transactional(readOnly = true)
    public TicketDetailClientResponseDto getTicketDetailInfo(long centerId) {
        Optional<Ticket> minimumPriceTicket = ticketRepository.getMinimumPriceTicket(centerId);
        if (minimumPriceTicket.isPresent()) {
            List<Review> reviewList = reviewRepository.findAllByCenterId(centerId);
            float score = getScore(reviewList);
            return TicketDetailClientResponseDto.of(score, reviewList.size(), minimumPriceTicket.get());
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, TicketInfoClientResponseDto> getHotTicketList(List<Long> centerIdList) {
        Map<Long, TicketInfoClientResponseDto> hotTicketResponseDtoMap = new HashMap<>();
        centerIdList.forEach((i) -> {
            Optional<PurchaseHistoryInOneWeekResponseDto> hotTicketIdInOneWeek = purchaseHistoryRepository.getHotTicketIdInOneWeek(i);
            List<Review> reviewList = reviewRepository.findAllByCenterId(i);
            float score = getScore(reviewList);
            TicketInfoClientResponseDto ticketInfoClientResponseDto;
            if (hotTicketIdInOneWeek.isPresent()) {
                ticketInfoClientResponseDto = TicketInfoClientResponseDto.from(hotTicketIdInOneWeek.get(), score, reviewList.size());
            } else {
                PurchaseHistoryInOneWeekResponseDto ticketInfo = ticketRepository.getTicketHighestIssueCount(i);
                ticketInfoClientResponseDto = TicketInfoClientResponseDto.from(ticketInfo, score, reviewList.size());
            }
            hotTicketResponseDtoMap.put(i, ticketInfoClientResponseDto);
        });
        return hotTicketResponseDtoMap;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, TicketInfoClientResponseDto> getRecommendTicketList(List<Long> centerIdList) {
        Map<Long, TicketInfoClientResponseDto> recommendTicketResponseDtoMap = new HashMap<>();
        centerIdList.forEach((i) -> {
            Optional<Ticket> minimumPriceTicket = ticketRepository.getMinimumPriceTicket(i);
            if (minimumPriceTicket.isPresent()) {
                List<Review> reviewList = reviewRepository.findAllByCenterId(i);
                float score = getScore(reviewList);
                TicketInfoClientResponseDto responseDto = TicketInfoClientResponseDto.of(minimumPriceTicket.get(), score, reviewList.size());
                recommendTicketResponseDtoMap.put(i, responseDto);
            }
        });
        return recommendTicketResponseDtoMap;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean getHasTicket(long centerId) {
        return ticketRepository.existsByCenterId(centerId);
    }

    private float getScore(List<Review> reviewList) {
        if (reviewList.size() == 0) return 0;
        float scoreSum = 0;
        for (Review review : reviewList) scoreSum += review.getScore();
        return scoreSum / reviewList.size();
    }
}
