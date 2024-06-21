package com.example.ticketservice.ticket.controller;

import com.example.ticketservice.common.util.Utils;
import com.example.ticketservice.ticket.dto.BaseResponseDto;
import com.example.ticketservice.ticket.service.LectureReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "수업 예약 API", description = "미구현")
public class LectureReservationController {
    private final LectureReservationService lectureReservationService;

    @Operation(summary = "수업 예약", description = "일단 lectureScheduleId에 아무 값을 넣어도 예약되게 구현함")
    @PostMapping("/lecture-reservation/{lectureScheduleId}/{userTicketId}")
    public ResponseEntity<BaseResponseDto<Long>> reserveLecture(
            @Parameter(description = "수업 일정 아이디", required = true)
            @PathVariable long lectureScheduleId,
            @Parameter(description = "사용자 이용권 아이디", required = true)
            @PathVariable long userTicketId,
            HttpServletRequest request) {
        long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.ok(new BaseResponseDto<>(lectureReservationService.reserveLecture(memberId, userTicketId, lectureScheduleId)));
    }
}
