package com.example.ticketservice.controller;

import com.example.ticketservice.common.util.Utils;
import com.example.ticketservice.dto.request.ReviewRequestDto;
import com.example.ticketservice.dto.response.AdminReviewResponseDto;
import com.example.ticketservice.dto.BaseResponseDto;
import com.example.ticketservice.dto.response.ReviewListResponseDto;
import com.example.ticketservice.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/reviews/{ticketId}")
    public ResponseEntity<BaseResponseDto<Long>> createReview(HttpServletRequest request,
                                                              @PathVariable long ticketId,
                                                              @RequestPart ReviewRequestDto requestDto,
                                                              @RequestPart List<MultipartFile> reviewImageList) {
        long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseDto<>(reviewService.createReview(memberId, ticketId, requestDto, reviewImageList)));
    }

    @GetMapping("/reviews/admin-page/{ticketId}/{reviewId}")
    public ResponseEntity<BaseResponseDto<AdminReviewResponseDto>> getAdminReviewList(@PathVariable long ticketId,
                                                                                      @PathVariable long reviewId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(reviewService.getAdminReviewList(ticketId, reviewId)));
    }

    @GetMapping("/reviews/count/{centerId}")
    public ResponseEntity<BaseResponseDto<Integer>> getReviewCountByCenterId(@PathVariable long centerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(reviewService.getReviewCountByCenterId(centerId)));
    }

    @GetMapping("/reviews/{ticketId}/{pageNo}/{sortId}")
    public ResponseEntity<BaseResponseDto<ReviewListResponseDto>> getReviewList(HttpServletRequest request,
                                                                                @PathVariable long ticketId,
                                                                                @PathVariable int pageNo,
                                                                                @PathVariable int sortId) {
        long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(reviewService.getReviewList(memberId, ticketId, pageNo, sortId)));
    }
}
