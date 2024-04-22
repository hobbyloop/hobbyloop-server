package com.example.ticketservice.controller;

import com.example.ticketservice.dto.BaseResponseDto;
import com.example.ticketservice.dto.response.CenterMemberResponseDto;
import com.example.ticketservice.dto.response.CenterMembershipDetailResponseDto;
import com.example.ticketservice.dto.response.UnapprovedUserTicketListResponseDto;
import com.example.ticketservice.service.CenterMembershipService;
import com.example.ticketservice.service.UserTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/center-membership")
public class CenterMembershipController {
    private final CenterMembershipService centerMembershipService;
    private final UserTicketService userTicketService;

    @GetMapping("/{centerId}/{pageNo}/{sortId}")
    public ResponseEntity<BaseResponseDto<List<CenterMemberResponseDto>>> getCenterMemberList(
            @PathVariable long centerId, @PathVariable int pageNo, @PathVariable int sortId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerMembershipService.getCenterMemberList(centerId, pageNo, sortId)));
    }

    @GetMapping("/{centerMembershipId}")
    public ResponseEntity<BaseResponseDto<CenterMembershipDetailResponseDto>> getCenterMembershipDetail(
            @PathVariable long centerMembershipId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerMembershipService.getCenterMembershipDetail(centerMembershipId)));
    }

    @GetMapping("/unapproved/{centerId}")
    public ResponseEntity<BaseResponseDto<List<UnapprovedUserTicketListResponseDto>>> getUnapprovedUserTicketList(@PathVariable long centerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(userTicketService.getUnapprovedUserTicketList(centerId)));
    }

    @PatchMapping("/{userTicketId}/approve")
    public ResponseEntity<BaseResponseDto<Void>> approveUserTicket(@PathVariable long userTicketId) {
        userTicketService.approveUserTicket(userTicketId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>());
    }
}
