package com.example.ticketservice.controller;

import com.example.ticketservice.common.util.Utils;
import com.example.ticketservice.dto.BaseResponseDto;
import com.example.ticketservice.service.LectureReservationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class LectureReservationController {
    private final LectureReservationService lectureReservationService;

    @PostMapping("/lecture-reservation/{lectureScheduleId}/{userTicketId}")
    public ResponseEntity<BaseResponseDto<Long>> reserveLecture(@PathVariable long lectureScheduleId,
                                                                @PathVariable long userTicketId,
                                                                HttpServletRequest request) {
        long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.ok(new BaseResponseDto<>(lectureReservationService.reserveLecture(memberId, userTicketId, lectureScheduleId)));
    }
}
