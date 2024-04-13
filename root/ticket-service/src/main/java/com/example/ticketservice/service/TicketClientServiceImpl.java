package com.example.ticketservice.service;

import com.example.ticketservice.client.dto.response.TicketClientResponseDto;
import com.example.ticketservice.client.dto.response.TicketDetailClientResponseDto;
import com.example.ticketservice.client.dto.response.TicketInfoClientResponseDto;
import com.example.ticketservice.dto.response.BookmarkScoreTicketResponseDto;
import com.example.ticketservice.dto.response.BookmarkTicketResponseDto;
import com.example.ticketservice.entity.Review;
import com.example.ticketservice.entity.Ticket;
import com.example.ticketservice.repository.review.ReviewRepository;
import com.example.ticketservice.repository.ticket.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TicketClientServiceImpl implements TicketClientService {

    private final TicketRepository ticketRepository;

    private final ReviewRepository reviewRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TicketClientResponseDto> getTicketClientResponseDto(long centerId) {
        List<Ticket> ticketList = ticketRepository.findAllByCenterId(centerId);
        return ticketList.stream().map(TicketClientResponseDto::from).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TicketDetailClientResponseDto getTicketDetailInfo(long centerId) {
        // TODO 상세 페이지 이용권 정보 조회
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, TicketInfoClientResponseDto> getHotTicketList(List<Long> centerIdList) {
        Map<Long, TicketInfoClientResponseDto> hotTicketResponseDtoMap = new HashMap<>();
        centerIdList.forEach((i) -> {
            // TODO 가장 많이 팔린 이용권 조회
        });
        return hotTicketResponseDtoMap;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, TicketInfoClientResponseDto> getRecommendTicketList(List<Long> centerIdList) {
        Map<Long, TicketInfoClientResponseDto> recommendTicketResponseDtoMap = new HashMap<>();
        centerIdList.forEach((i) -> {
            List<Review> reviewList = reviewRepository.findAllByCenterId(i);
            float score = getScore(reviewList);
            // TODO 최저가 이용권 조회
        });
        return recommendTicketResponseDtoMap;
    }

    private float getScore(List<Review> reviewList) {
        if (reviewList.size() == 0) return 0;
        float scoreSum = 0;
        for (Review review : reviewList) scoreSum += review.getScore();
        return scoreSum / reviewList.size();
    }
}
