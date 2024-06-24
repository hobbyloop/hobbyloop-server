package com.example.ticketservice.point.controller;

import com.example.ticketservice.common.security.RoleAuthorization;
import com.example.ticketservice.point.dto.PointHistoryListResponseDto;
import com.example.ticketservice.point.service.PointService;
import com.example.ticketservice.common.util.Utils;
import com.example.ticketservice.ticket.dto.BaseResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/points")
@Tag(name = "포인트 API")
public class PointController {

    private final PointService pointService;

    @GetMapping("/histories")
    @RoleAuthorization(roles = {"USER"})
    @Operation(summary = "포인트 사용 내역 조회", description = "[피그마 링크](https://www.figma.com/design/ShgCuih6scznAlHzHNz8Jo/2024-%ED%95%98%EB%B9%84%EB%A3%A8%ED%94%84_dev?node-id=1687-50997&t=da28ryPWiX4Q2W9O-4)")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PointHistoryListResponseDto.class)))
    public ResponseEntity<BaseResponseDto<PointHistoryListResponseDto>> getPointHistories(HttpServletRequest request) {
        Long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(pointService.getPointHistory(memberId)));
    }
}
