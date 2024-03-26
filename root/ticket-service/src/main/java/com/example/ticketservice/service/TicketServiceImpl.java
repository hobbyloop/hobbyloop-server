package com.example.ticketservice.service;

import com.example.ticketservice.client.CompanyServiceClient;
import com.example.ticketservice.client.dto.response.CenterInfoResponseDto;
import com.example.ticketservice.dto.request.TicketCreateRequestDto;
import com.example.ticketservice.dto.response.*;
import com.example.ticketservice.entity.Review;
import com.example.ticketservice.entity.Ticket;
import com.example.ticketservice.repository.review.ReviewRepository;
import com.example.ticketservice.repository.ticket.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService{

    private final TicketRepository ticketRepository;

    private final CompanyServiceClient companyServiceClient;

    private final AmazonS3Service amazonS3Service;

    private final ReviewRepository reviewRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TicketResponseDto> getTicketList(long centerId) {
        List<Ticket> ticketList = ticketRepository.findAllByCenterId(centerId);
        return ticketList.stream().map(TicketResponseDto::from).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminTicketResponseDto> getAdminTicketList(long centerId, long ticketId) {
        List<Ticket> ticketList = ticketRepository.getTicketList(centerId, ticketId);
        CenterInfoResponseDto centerInfo = companyServiceClient.getCenterInfo(centerId).getData();
        return ticketList.stream().map(t -> AdminTicketResponseDto.of(centerInfo, t)).toList();
    }

    @Override
    @Transactional
    public TicketCreateResponseDto createTicket(long centerId, TicketCreateRequestDto requestDto, MultipartFile ticketImage) {
        String ticketImageKey = saveS3Img(ticketImage);
        String ticketImageUrl = amazonS3Service.getFileUrl(ticketImageKey);
        Ticket ticket = Ticket.of(centerId, ticketImageKey, ticketImageUrl, requestDto);
        Ticket saveTicket = ticketRepository.save(ticket);
        CenterInfoResponseDto centerInfo = companyServiceClient.getCenterInfo(centerId).getData();
        return TicketCreateResponseDto.of(centerInfo, saveTicket);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, BookmarkScoreTicketResponseDto> getBookmarkTicketList(List<Long> centerIdList) {
        Map<Long, BookmarkScoreTicketResponseDto> bookmarkTicketResponseDtoMap = new HashMap<>();
        centerIdList.forEach((i) -> {
            float score = getScore(i);
            List<Ticket> ticketList = ticketRepository.findAllByCenterId(i);
            List<BookmarkTicketResponseDto> bookmarkTicketResponseDtoList = ticketList.stream().map(BookmarkTicketResponseDto::from).toList();
            BookmarkScoreTicketResponseDto bookmarkScoreTicketResponseDto = BookmarkScoreTicketResponseDto.of(score, bookmarkTicketResponseDtoList);
            bookmarkTicketResponseDtoMap.put(i, bookmarkScoreTicketResponseDto);
        });
        return bookmarkTicketResponseDtoMap;
    }

    private float getScore(long centerId) {
        List<Review> reviewList = reviewRepository.findAllByCenterId(centerId);
        if (reviewList.size() == 0) return 0;
        float scoreSum = 0;
        for (Review review : reviewList) scoreSum += review.getScore();
        return scoreSum / reviewList.size();
    }

    private String saveS3Img(MultipartFile profileImg) {
        try {
            return amazonS3Service.upload(profileImg, "CenterImage");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
