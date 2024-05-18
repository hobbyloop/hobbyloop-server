package com.example.ticketservice.controller;

import com.example.ticketservice.client.dto.response.TicketClientBaseResponseDto;
import com.example.ticketservice.client.dto.response.TicketDetailClientResponseDto;
import com.example.ticketservice.client.dto.response.TicketInfoClientResponseDto;
import com.example.ticketservice.dto.BaseResponseDto;
import com.example.ticketservice.dto.response.BookmarkScoreTicketResponseDto;
import com.example.ticketservice.service.TicketClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/client/tickets")
public class TicketClientController {

    private final TicketClientService ticketClientService;

    @GetMapping("/{centerId}")
    public ResponseEntity<BaseResponseDto<TicketClientBaseResponseDto>> getTicketList(@PathVariable(value = "centerId") long centerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketClientService.getTicketList(centerId)));
    }

    @PostMapping("/bookmark-ticket-list")
    public ResponseEntity<BaseResponseDto<Map<Long, BookmarkScoreTicketResponseDto>>> getBookmarkTicketList(@RequestBody List<Long> centerIdList) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketClientService.getBookmarkTicketList(centerIdList)));
    }

    @GetMapping("/detail/{centerId}")
    public ResponseEntity<BaseResponseDto<TicketDetailClientResponseDto>> getTicketDetailInfo(@PathVariable(value = "centerId") long centerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketClientService.getTicketDetailInfo(centerId)));
    }

    @PostMapping("/hot-ticket-list")
    public ResponseEntity<BaseResponseDto<Map<Long, TicketInfoClientResponseDto>>> getHotTicketList(@RequestBody List<Long> centerIdList) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketClientService.getHotTicketList(centerIdList)));
    }

    @PostMapping("/recommend-ticket-list")
    public ResponseEntity<BaseResponseDto<Map<Long, TicketInfoClientResponseDto>>> getRecommendTicketList(@RequestBody List<Long> centerIdList) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketClientService.getRecommendTicketList(centerIdList)));
    }

    @GetMapping("/has-ticket/{centerId}")
    public ResponseEntity<BaseResponseDto<Boolean>> getHasTicket(@PathVariable(value = "centerId") long centerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(ticketClientService.getHasTicket(centerId)));
    }
}
