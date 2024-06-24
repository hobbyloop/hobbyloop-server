package com.example.ticketservice.coupon.controller;

import com.example.ticketservice.common.exception.ExceptionEnum;
import com.example.ticketservice.common.swagger.ApiExceptionResponse;
import com.example.ticketservice.common.util.Utils;
import com.example.ticketservice.coupon.dto.CouponResponseDto;
import com.example.ticketservice.coupon.dto.MemberCouponResponseDto;
import com.example.ticketservice.coupon.service.CouponService;
import com.example.ticketservice.ticket.dto.BaseResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupons")
@Tag(name = "쿠폰 API")
public class CouponController {
    private final CouponService couponService;

    @GetMapping("/members")
    @Operation(summary = "마이페이지 - 회원 보유 쿠폰 목록 조회", description = "[피그마 링크](https://www.figma.com/design/ShgCuih6scznAlHzHNz8Jo/2024-%ED%95%98%EB%B9%84%EB%A3%A8%ED%94%84_dev?node-id=1687-51095&t=M9ouZA6XZyDUB2QN-4)")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = MemberCouponResponseDto.class))))
    public ResponseEntity<BaseResponseDto<List<MemberCouponResponseDto>>> getAvailableMemberCouponList(
            HttpServletRequest request
    ) {
        Long memberId = Utils.parseAuthorizedId(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(couponService.getAvailableMemberCoupons(memberId)));
    }

    @GetMapping("/{centerId}")
    @Operation(summary = "시설 상세 조회 - 시설 쿠폰 목록 조회", description = "해당 시설에 사용 가능한 쿠폰 목록 조회/n[피그마 링크](https://www.figma.com/file/ShgCuih6scznAlHzHNz8Jo?embed_host=notion&kind=file&node-id=1687-54965&t=8wZnHMeTipE3WkFU-4&viewer=1)")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CouponResponseDto.class))))
    public ResponseEntity<BaseResponseDto<List<CouponResponseDto>>> getCenterCouponList(
            @PathVariable(value = "centerId") Long centerId,
            HttpServletRequest request
    ) {
        Long memberId = Utils.parseAuthorizedId(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(couponService.getCenterCouponListForMember(memberId, centerId)));
    }

    @PostMapping("/issue/{couponId}")
    @Operation(summary = "시설 상세 조회 - 시설 쿠폰 목록 조회 - 단일 쿠폰 다운로드", description = "[피그마 링크](https://www.figma.com/design/ShgCuih6scznAlHzHNz8Jo/2024-%ED%95%98%EB%B9%84%EB%A3%A8%ED%94%84_dev?node-id=1687-54965&t=M9ouZA6XZyDUB2QN-4)")
    @ApiResponse(responseCode = "201", description = "성공 시 발급된 쿠폰 아이디 반환", content = @Content(schema = @Schema(implementation = Long.class)))
    @ApiExceptionResponse({
            ExceptionEnum.COUPON_NOT_EXIST_EXCEPTION,
            ExceptionEnum.COUPON_ALREADY_ISSUED_EXCEPTION,
            ExceptionEnum.COUPON_OUT_OF_DATE_EXCEPTION,
            ExceptionEnum.COUPON_SOLD_OUT_EXCEPTION
    })
    public ResponseEntity<BaseResponseDto<Long>> issueSingleCoupon(
            @PathVariable(value = "couponId") Long couponId,
            HttpServletRequest request
    ) {
        Long memberId = Utils.parseAuthorizedId(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseDto<>(couponService.issueSingleCoupon(memberId, couponId)));
    }


    @PostMapping("/issue/all")
    @Operation(summary = "시설 상세 조회 - 시설 쿠폰 목록 조회 - 전체 쿠폰 다운로드", description = "alreadyIssued가 true인 쿠폰은 제외해주세요.\n[피그마 링크](https://www.figma.com/design/ShgCuih6scznAlHzHNz8Jo/2024-%ED%95%98%EB%B9%84%EB%A3%A8%ED%94%84_dev?node-id=1687-54965&t=M9ouZA6XZyDUB2QN-4)")
    @ApiResponse(responseCode = "201", description = "성공", content = @Content(schema = @Schema(implementation = BaseResponseDto.class)))
    public ResponseEntity<BaseResponseDto<Void>> issueAllCoupons(
            @RequestParam(value = "couponIds") List<Long> couponIds,
            HttpServletRequest request
    ) {
        Long memberId = Utils.parseAuthorizedId(request);

        couponService.issueAllCoupons(memberId, couponIds);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseDto<>());
    }
}
