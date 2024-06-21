package com.example.ticketservice.ticket.service;

import com.example.ticketservice.ticket.client.CompanyServiceClient;
import com.example.ticketservice.ticket.client.dto.response.CenterDistanceInfoResponseDto;
import com.example.ticketservice.ticket.client.dto.response.CenterInfoResponseDto;
import com.example.ticketservice.ticket.client.dto.response.OriginalBusinessResponseDto;
import com.example.ticketservice.common.exception.ApiException;
import com.example.ticketservice.common.exception.ExceptionEnum;
import com.example.ticketservice.ticket.dto.BaseResponseDto;
import com.example.ticketservice.ticket.dto.request.TicketCreateRequestDto;
import com.example.ticketservice.ticket.dto.request.TicketUpdateRequestDto;
import com.example.ticketservice.ticket.entity.CategoryEnum;
import com.example.ticketservice.ticket.entity.Review;
import com.example.ticketservice.ticket.entity.Ticket;
import com.example.ticketservice.ticket.entity.UserTicket;
import com.example.ticketservice.ticket.repository.ReviewImageRepository;
import com.example.ticketservice.ticket.repository.review.ReviewRepository;
import com.example.ticketservice.ticket.repository.ticket.TicketRepository;
import com.example.ticketservice.ticket.dto.response.*;
import com.example.ticketservice.ticket.repository.ticket.UserTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService{

    private final TicketRepository ticketRepository;

    private final UserTicketRepository userTicketRepository;

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
        List<Review> reviewList = reviewRepository.findAllByCenterId(ticket.getCenterId());
        float score = getScore(reviewList);
        return AdminReviewTicketResponseDto.of(centerInfo.getData(), ticket, score);
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewListTicketResponseDto getIOSTicketInfo(long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.TICKET_NOT_EXIST_EXCEPTION));
        List<String> totalImageUrlList = reviewImageRepository.findAllUrlByTicketId(ticket.getId());
        List<Review> reviewList = reviewRepository.findAllByCenterId(ticket.getCenterId());
        float score = getScore(reviewList);
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

        List<UserTicket> userTickets = userTicketRepository.findAllByTicketId(ticketId);
        if (!userTickets.isEmpty()) {
            throw new ApiException(ExceptionEnum.TICKET_CANNOT_CANCEL_UPLOAD);
        }

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

    @Override
    @Transactional(readOnly = true)
    public List<CategoryTicketResponseDto> getCategoryTicket(long memberId,
                                                             String category,
                                                             int sortId,
                                                             int refundable,
                                                             double score,
                                                             int pageNo,
                                                             int allowLocation,
                                                             Double latitude,
                                                             Double longitude,
                                                             List<String> locations) {
        Map<Long, CenterDistanceInfoResponseDto> centerInfoMap = new HashMap<>();
        int categoryId = CategoryEnum.findByName(category).getCategoryType();
        List<CategoryTicketResponseDto> result = new ArrayList<>();
        while (true) {
            List<Ticket> ticketList = ticketRepository.getTicketListByCategory(categoryId, sortId, score, pageNo);
            boolean isResultSizeGoe20 = false;

            for (Ticket ticket : ticketList) {
                CenterDistanceInfoResponseDto centerInfo;
                if (centerInfoMap.containsKey(ticket.getCenterId())) {
                    centerInfo = centerInfoMap.get(ticket.getCenterId());
                } else {
                    centerInfo = companyServiceClient.getCenterDistanceInfo(
                            ticket.getCenterId(),
                            memberId,
                            refundable,
                            allowLocation,
                            latitude,
                            longitude,
                            locations
                    ).getData();
                    centerInfoMap.put(ticket.getCenterId(), centerInfo);
                }

                if (!centerInfo.isSatisfied()) continue;

                List<Review> reviewList = reviewRepository.findAllByCenterId(ticket.getCenterId());
                float reviewScore = getScore(reviewList);

                result.add(CategoryTicketResponseDto.of(centerInfo, ticket.getCalculatedPrice(), reviewScore, reviewList.size()));
                if (result.size() >= 20) {
                    isResultSizeGoe20 = true;
                    break;
                }
            }

            if (isResultSizeGoe20) break;
        }
        return result;
    }

    private float getScore(List<Review> reviewList) {
        if (reviewList.size() == 0) return 0;
        float scoreSum = 0;
        for (Review review : reviewList) scoreSum += review.getScore();
        return scoreSum / reviewList.size();
    }
}
