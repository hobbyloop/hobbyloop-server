package com.example.ticketservice.pay.controller;

import com.example.ticketservice.common.util.Utils;
import com.example.ticketservice.pay.dto.request.CheckoutRequestDto;
import com.example.ticketservice.pay.dto.response.CheckoutPrepareResponseDto;
import com.example.ticketservice.pay.dto.response.CheckoutResponseDto;
import com.example.ticketservice.pay.service.PaymentService;
import com.example.ticketservice.ticket.dto.BaseResponseDto;
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
    public ResponseEntity<BaseResponseDto<CheckoutPrepareResponseDto>> prepareCheckout(
            @PathVariable(value = "ticketId") Long ticketId,
            HttpServletRequest request
    ) {
        Long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseDto<>(paymentService.prepareCheckout(memberId, ticketId)));
    }

    @PostMapping("/checkout")
    public ResponseEntity<BaseResponseDto<CheckoutResponseDto>> checkout(
            @RequestBody CheckoutRequestDto requestDto,
            HttpServletRequest request
    ) {
        Long memberId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseDto<>(paymentService.checkout(memberId, requestDto)));
    }

}
