package com.example.ticketservice.ticket.controller;

import com.example.ticketservice.common.exception.ExceptionEnum;
import com.example.ticketservice.common.swagger.ApiExceptionResponse;
import com.example.ticketservice.ticket.dto.BaseResponseDto;
import com.example.ticketservice.ticket.dto.request.CenterMembershipJoinRequestDto;
import com.example.ticketservice.ticket.dto.response.centermembership.CenterMemberResponseDto;
import com.example.ticketservice.ticket.dto.response.centermembership.CenterMembershipDetailResponseDto;
import com.example.ticketservice.ticket.dto.response.centermembership.CenterMembershipJoinedResponseDto;
import com.example.ticketservice.ticket.dto.response.userticket.AvailableUserTicketsWithCenterInfo;
import com.example.ticketservice.ticket.dto.response.userticket.UnapprovedUserTicketListResponseDto;
import com.example.ticketservice.ticket.service.CenterMembershipService;
import com.example.ticketservice.ticket.service.UserTicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/center-membership")
@Tag(name = "관리자 시설 회원 API")
public class CenterMembershipController {
    private final CenterMembershipService centerMembershipService;
    private final UserTicketService userTicketService;

    @PostMapping("/{centerId}/{memberId}")
    @Operation(summary = "시설 회원 직접 등록", description = "관리자가 현장에서 시설 회원 정보를 직접 등록함\n[피그마 링크](https://www.figma.com/design/ShgCuih6scznAlHzHNz8Jo/2024-%ED%95%98%EB%B9%84%EB%A3%A8%ED%94%84_dev?node-id=1687-64106&t=hLqGNykZUNOi0vhM-4)")
    @ApiResponse(responseCode = "201", description = "성공", content = @Content(schema = @Schema(implementation = CenterMembershipJoinedResponseDto.class)))
    @ApiExceptionResponse({
            ExceptionEnum.CENTER_MEMBERSHIP_ALREADY_JOINED_EXCEPTION,
            ExceptionEnum.TICKET_NOT_EXIST_EXCEPTION
    })
    public ResponseEntity<BaseResponseDto<CenterMembershipJoinedResponseDto>> joinCenterMembershipByAdmin(
            @Parameter(description = "업체 아이디", required = true)
            @PathVariable Long centerId,
            @Parameter(description = "회원 아이디", required = true)
            @PathVariable Long memberId,
            @RequestBody CenterMembershipJoinRequestDto requestDto
            ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseDto<>(centerMembershipService.joinCenterMembershipByAdmin(centerId, memberId, requestDto)));
    }

    @GetMapping("/{centerId}/{pageNo}/{sortId}")
    @Operation(summary = "시설 회원 목록 조회", description = "[피그마 링크](https://www.figma.com/design/ShgCuih6scznAlHzHNz8Jo/2024-%ED%95%98%EB%B9%84%EB%A3%A8%ED%94%84_dev?node-id=1687-63957&t=hLqGNykZUNOi0vhM-4)")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CenterMemberResponseDto.class))))
    public ResponseEntity<BaseResponseDto<List<CenterMemberResponseDto>>> getCenterMemberList(
            @Parameter(description = "업체 아이디", required = true)
            @PathVariable long centerId,
            @Parameter(description = "페이지 번호(1페이지 크기: 20)", example = "1", required = true)
            @PathVariable int pageNo,
            @Parameter(description = "정렬 기준, 0: 이름순, 1: 이용권별(미구현), 2: 만료회원 필터", example = "0", required = true)
            @PathVariable int sortId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerMembershipService.getCenterMemberList(centerId, pageNo, sortId)));
    }

    @GetMapping("/{centerMembershipId}")
    @Operation(summary = "시설 회원 상세 정보 조회", description = "[피그마 링크](https://www.figma.com/design/ShgCuih6scznAlHzHNz8Jo/2024-%ED%95%98%EB%B9%84%EB%A3%A8%ED%94%84_dev?node-id=1687-63654&t=hLqGNykZUNOi0vhM-4)")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CenterMembershipDetailResponseDto.class)))
    @ApiExceptionResponse({
            ExceptionEnum.CENTER_MEMBERSHIP_NOT_EXIST_EXCEPTION
    })
    public ResponseEntity<BaseResponseDto<CenterMembershipDetailResponseDto>> getCenterMembershipDetail(
            @Parameter(description = "시설 회원 아이디", required = true)
            @PathVariable long centerMembershipId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerMembershipService.getCenterMembershipDetail(centerMembershipId)));
    }

    @GetMapping("/unapproved/{centerId}")
    @Operation(summary = "이용권 신청 목록 조회", description = "사용자가 이용권을 구매했으나 관리자가 승인하지 않은 상태의 이용권 목록\n[피그마 링크](https://www.figma.com/design/ShgCuih6scznAlHzHNz8Jo/2024-%ED%95%98%EB%B9%84%EB%A3%A8%ED%94%84_dev?node-id=1687-64033&t=hLqGNykZUNOi0vhM-4)")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UnapprovedUserTicketListResponseDto.class))))
    public ResponseEntity<BaseResponseDto<List<UnapprovedUserTicketListResponseDto>>> getUnapprovedUserTicketList(
            @Parameter(description = "업체 아이디", required = true)
            @PathVariable long centerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(userTicketService.getUnapprovedUserTicketList(centerId)));
    }

    @PatchMapping("/{userTicketId}/approve")
    @Operation(summary = "이용권 승인", description = "[피그마 링크](https://www.figma.com/design/ShgCuih6scznAlHzHNz8Jo/2024-%ED%95%98%EB%B9%84%EB%A3%A8%ED%94%84_dev?node-id=1687-64033&t=hLqGNykZUNOi0vhM-4)")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = BaseResponseDto.class)))
    @ApiExceptionResponse({
            ExceptionEnum.USER_TICKET_NOT_EXIST_EXCEPTION
    })
    public ResponseEntity<BaseResponseDto<Void>> approveUserTicket(
            @Parameter(description = "사용자 이용권 아이디", required = true)
            @PathVariable long userTicketId) {
        userTicketService.approveUserTicket(userTicketId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>());
    }
}
