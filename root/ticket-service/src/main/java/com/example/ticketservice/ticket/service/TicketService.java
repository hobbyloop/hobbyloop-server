package com.example.ticketservice.ticket.service;

import com.example.ticketservice.ticket.client.dto.response.TicketClientForLectureResponseDto;
import com.example.ticketservice.ticket.dto.request.TicketCreateRequestDto;
import com.example.ticketservice.ticket.dto.request.TicketUpdateRequestDto;
import com.example.ticketservice.ticket.dto.response.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TicketService {
    List<TicketResponseDto> getTicketList(long centerId, long ticketId);

    List<AdminTicketResponseDto> getAdminTicketList(long centerId, long ticketId);

    TicketCreateResponseDto createTicket(long centerId, TicketCreateRequestDto requestDto, MultipartFile ticketImage);

    AdminReviewTicketResponseDto getTicketInfo(long ticketId);

    ReviewListTicketResponseDto getIOSTicketInfo(long ticketId);

    TicketDetailResponseDto getTicketDetail(long ticketId);

    TicketDetailResponseDto updateTicket(long ticketId, TicketUpdateRequestDto requestDto, MultipartFile ticketImage);

    void uploadTicket(long ticketId);

    void cancelUploadTicket(long ticketId);

    List<TicketByCenterResponseDto> getTicketListByCenter(long centerId);

    List<AdminMyTicketResponseDto> getMyTicketList(long centerId);

    List<CategoryTicketResponseDto> getCategoryTicketAroundMe(long memberId, String category, int sortId, int refundable, double score, int allowLocation, double latitude, double longitude, int distance);

    List<CategoryTicketResponseDto> getCategoryTicket(long memberId, String category, int sortId, int refundable, double score, int pageNo, List<String> locations);

    List<TicketClientForLectureResponseDto> getTicketListForLecture(long centerId);

}
