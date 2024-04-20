package com.example.ticketservice.service;

import com.example.ticketservice.client.CompanyServiceClient;
import com.example.ticketservice.client.MemberServiceClient;
import com.example.ticketservice.client.dto.response.CenterInfoResponseDto;
import com.example.ticketservice.client.dto.response.MemberInfoResponseDto;
import com.example.ticketservice.client.dto.response.OriginalBusinessResponseDto;
import com.example.ticketservice.common.exception.ApiException;
import com.example.ticketservice.common.exception.ExceptionEnum;
import com.example.ticketservice.dto.BaseResponseDto;
import com.example.ticketservice.dto.request.TicketCreateRequestDto;
import com.example.ticketservice.dto.request.TicketUpdateRequestDto;
import com.example.ticketservice.dto.response.*;
import com.example.ticketservice.entity.Review;
import com.example.ticketservice.entity.Ticket;
import com.example.ticketservice.entity.UserTicket;
import com.example.ticketservice.repository.ReviewImageRepository;
import com.example.ticketservice.repository.review.ReviewRepository;
import com.example.ticketservice.repository.ticket.TicketRepository;
import com.example.ticketservice.repository.ticket.UserTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService{

    private final TicketRepository ticketRepository;

    private final CompanyServiceClient companyServiceClient;

    private final AmazonS3Service amazonS3Service;

    private final ReviewRepository reviewRepository;

    private final ReviewService reviewService;

    private final ReviewImageRepository reviewImageRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TicketResponseDto> getTicketList(long centerId, long ticketId) {
        List<Ticket> ticketList = ticketRepository.getTicketList(centerId, ticketId);
        CenterInfoResponseDto centerInfo = companyServiceClient.getCenterInfo(centerId).getData();
        return ticketList.stream().map(t -> TicketResponseDto.from(t, centerInfo)).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminTicketResponseDto> getAdminTicketList(long centerId, long ticketId) {
        List<Ticket> ticketList = ticketRepository.getTicketList(centerId, ticketId);
        CenterInfoResponseDto centerInfo = companyServiceClient.getCenterInfo(centerId).getData();
        return ticketList.stream().map(t -> AdminTicketResponseDto.of(centerInfo, t, reviewService.getScore(t.getId()))).toList();
    }

    @Override
    @Transactional
    public TicketCreateResponseDto createTicket(long centerId, TicketCreateRequestDto requestDto, MultipartFile ticketImage) {
        String ticketImageKey = amazonS3Service.saveS3Img(ticketImage, "TicketImage");
        String ticketImageUrl = amazonS3Service.getFileUrl(ticketImageKey);
        Ticket ticket = Ticket.of(centerId, ticketImageKey, ticketImageUrl, requestDto);
        Ticket saveTicket = ticketRepository.save(ticket);
        CenterInfoResponseDto centerInfo = companyServiceClient.getCenterInfo(centerId).getData();
        return TicketCreateResponseDto.of(centerInfo, saveTicket);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminReviewTicketResponseDto getTicketInfo(long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.TICKET_NOT_EXIST_EXCEPTION));
        BaseResponseDto<CenterInfoResponseDto> centerInfo = companyServiceClient.getCenterInfo(ticket.getCenterId());
        float score = getScore(ticketId);
        return AdminReviewTicketResponseDto.of(centerInfo.getData(), ticket, score);
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewListTicketResponseDto getIOSTicketInfo(long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.TICKET_NOT_EXIST_EXCEPTION));
        List<String> totalImageUrlList = reviewImageRepository.findAllUrlByTicketId(ticket.getId());
        float score = getScore(ticketId);
        return ReviewListTicketResponseDto.of(score, totalImageUrlList);
    }

    @Override
    @Transactional
    public TicketDetailResponseDto getTicketDetail(long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.TICKET_NOT_EXIST_EXCEPTION));
        CenterInfoResponseDto centerInfo = companyServiceClient.getCenterInfo(ticket.getCenterId()).getData();
        OriginalBusinessResponseDto businessInfo = companyServiceClient.getOriginalBusinessInfo(ticket.getCenterId()).getData();
        // TODO: lectureInfo도 필요함
        return TicketDetailResponseDto.of(ticket, centerInfo, businessInfo);
    }

    @Override
    @Transactional
    public TicketDetailResponseDto updateTicket(long ticketId, TicketUpdateRequestDto requestDto, MultipartFile ticketImage) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.TICKET_NOT_EXIST_EXCEPTION));

        if (ticketImage != null) {
            String ticketImageKey = amazonS3Service.saveS3Img(ticketImage, "TicketImage");
            String ticketImageUrl = amazonS3Service.getFileUrl(ticketImageKey);
            ticket.updateTicketImage(ticketImageKey, ticketImageUrl);
        }

        ticket.update(requestDto);
        return getTicketDetail(ticketId);
    }

    @Override
    @Transactional
    public void uploadTicket(long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.TICKET_NOT_EXIST_EXCEPTION));
        ticket.upload();
    }

    @Override
    @Transactional
    public void cancelUploadTicket(long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.TICKET_NOT_EXIST_EXCEPTION));
        ticket.cancelUpload();
    }

    @Override
    @Transactional
    public List<TicketByCenterResponseDto> getTicketListByCenter(long centerId) {
        List<Ticket> ticketList = ticketRepository.findAllByCenterIdAndIsUploadTrueOrderByCalculatedPriceAsc(centerId);
        return ticketList.stream()
                .map(TicketByCenterResponseDto::from)
                .toList();
    }

    @Override
    @Transactional
    public List<AdminMyTicketResponseDto> getMyTicketList(long centerId) {
        List<Ticket> ticketList = ticketRepository.findAllByCenterIdAndIsUploadTrueOrderByCreatedAtDesc(centerId);
        CenterInfoResponseDto centerInfo = companyServiceClient.getCenterInfo(centerId).getData();
        return ticketList.stream().map(t -> AdminMyTicketResponseDto.from(t, centerInfo)).toList();
    }

    private float getScore(long centerId) {
        List<Review> reviewList = reviewRepository.findAllByCenterId(centerId);
        if (reviewList.size() == 0) return 0;
        float scoreSum = 0;
        for (Review review : reviewList) scoreSum += review.getScore();
        return scoreSum / reviewList.size();
    }
}
