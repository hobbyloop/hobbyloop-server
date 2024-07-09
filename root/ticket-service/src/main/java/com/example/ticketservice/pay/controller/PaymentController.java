package com.example.ticketservice.pay.controller;

import com.example.ticketservice.common.exception.ExceptionEnum;
import com.example.ticketservice.common.swagger.ApiExceptionResponse;
import com.example.ticketservice.common.util.Utils;
import com.example.ticketservice.pay.dto.request.CheckoutRequestDto;
import com.example.ticketservice.pay.dto.request.PaymentConfirmRequestDto;
import com.example.ticketservice.pay.dto.response.CheckoutPrepareResponseDto;
import com.example.ticketservice.pay.dto.response.CheckoutResponseDto;
import com.example.ticketservice.pay.dto.response.PaymentConfirmResponseDto;
import com.example.ticketservice.pay.service.PaymentService;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
@Tag(name = "결제 API")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/checkout/prepare/{ticketId}")
    @Operation(summary = "체크아웃 준비", description = "결제로 넘어가기 전, 최종 결제 정보를 수집하고 준비함\n[피그마 링크1](https://www.figma.com/design/ShgCuih6scznAlHzHNz8Jo/2024-%ED%95%98%EB%B9%84%EB%A3%A8%ED%94%84_dev?node-id=1687-50808&t=QSg5tOX9invaIClt-4)\n[피그마 링크2](https://www.figma.com/design/ShgCuih6scznAlHzHNz8Jo/2024-%ED%95%98%EB%B9%84%EB%A3%A8%ED%94%84_dev?node-id=1687-50893&t=QSg5tOX9invaIClt-4)")
    @ApiResponse(responseCode = "201", description = "성공", content = @Content(schema = @Schema(implementation = CheckoutPrepareResponseDto.class)))
    @ApiExceptionResponse({
            ExceptionEnum.TICKET_NOT_EXIST_EXCEPTION
    })
    public ResponseEntity<BaseResponseDto<CheckoutPrepareResponseDto>> prepareCheckout(
            @PathVariable(value = "ticketId") Long ticketId,
            HttpServletRequest request
    ) {
        Long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseDto<>(paymentService.prepareCheckout(memberId, ticketId)));
    }

    @PostMapping("/checkout")
    @Operation(summary = "체크아웃", description = "최종 결제 정보를 바탕으로 체크아웃 후 불완전한 결제 데이터 생성\n[피그마 링크]()")
    @ApiResponse(responseCode = "201", description = "성공", content = @Content(schema = @Schema(implementation = CheckoutResponseDto.class)))
    @ApiExceptionResponse({
            ExceptionEnum.CHECKOUT_NOT_EXIST_EXCEPTION
    })
    public ResponseEntity<BaseResponseDto<CheckoutResponseDto>> checkout(
            @RequestBody CheckoutRequestDto requestDto,
            HttpServletRequest request
    ) {
        Long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseDto<>(paymentService.checkout(memberId, requestDto)));
    }

    @PostMapping("/confirm")
    @Operation(summary = "결제 승인", description = "클라이언트에서 결제 진행 후, PSP에 결제 승인 여부 확인")
    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PaymentConfirmResponseDto.class)))
    @ApiExceptionResponse({
            ExceptionEnum.PAYMENT_NOT_EXIST_EXCEPTION,
            ExceptionEnum.PAYMENT_AMOUNT_MISMATCH_EXCEPTION,
            ExceptionEnum.UNAUTHORIZED_PAYMENT_REQUEST_EXCEPTION
    })
    public ResponseEntity<BaseResponseDto<PaymentConfirmResponseDto>> confirm(
            @RequestBody PaymentConfirmRequestDto requestDto,
            HttpServletRequest request
    ) {
        Long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(paymentService.confirm(memberId, requestDto)));
    }

    @PostMapping("/refund/{paymentId}")
    public ResponseEntity<BaseResponseDto<PaymentConfirmResponseDto>> refund(
            @PathVariable(value = "paymentId") Long paymentId,
            HttpServletRequest request
    ) {
        Long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(paymentService.refund(memberId, paymentId)));
    }

}
