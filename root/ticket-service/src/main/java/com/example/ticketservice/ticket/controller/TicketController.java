package com.example.ticketservice.ticket.controller;

import com.example.ticketservice.ticket.common.util.Utils;
import com.example.ticketservice.ticket.dto.BaseResponseDto;
import com.example.ticketservice.ticket.service.TicketService;
import com.example.ticketservice.ticket.dto.response.CategoryTicketResponseDto;
import com.example.ticketservice.ticket.dto.response.ReviewListTicketResponseDto;
import com.example.ticketservice.ticket.dto.response.TicketByCenterResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tickets")
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/ios-review/{ticketId}")
    public ResponseEntity<BaseResponseDto<ReviewListTicketResponseDto>> getIOSTicketInfo(@PathVariable long ticketId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketService.getIOSTicketInfo(ticketId)));
    }

    @GetMapping("/centers/{centerId}")
    public ResponseEntity<BaseResponseDto<List<TicketByCenterResponseDto>>> getTicketListByCenter(@PathVariable long centerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketService.getTicketListByCenter(centerId)));
    }

    @GetMapping ("/category/{category}/{sortId}/{refundable}/{score}/{pageNo}/{allow-location}/{latitude}/{longitude}")
    public ResponseEntity<BaseResponseDto<List<CategoryTicketResponseDto>>> getCategoryTicket(HttpServletRequest request,
                                                                                              @PathVariable(value = "category") String category,
                                                                                              @PathVariable(value = "sortId") int sortId,
                                                                                              @PathVariable(value = "refundable") int refundable,
                                                                                              @PathVariable(value = "score") double score,
                                                                                              @PathVariable(value = "pageNo") int pageNo,
                                                                                              @PathVariable(value = "allow-location") int allowLocation,
                                                                                              @PathVariable(value = "latitude") Double latitude,
                                                                                              @PathVariable(value = "longitude") Double longitude,
                                                                                              @RequestParam(value = "location") List<String> locations) {
        long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketService.getCategoryTicket(memberId, category, sortId, refundable, score, pageNo, allowLocation, latitude, longitude, locations)));
    }
}
