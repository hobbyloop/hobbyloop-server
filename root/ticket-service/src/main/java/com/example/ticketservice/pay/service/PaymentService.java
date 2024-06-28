package com.example.ticketservice.pay.service;

import com.example.ticketservice.pay.dto.request.CheckoutRequestDto;
import com.example.ticketservice.pay.dto.response.CheckoutPrepareResponseDto;
import com.example.ticketservice.pay.dto.response.CheckoutResponseDto;

public interface PaymentService {
    CheckoutPrepareResponseDto prepareCheckout(Long memberId, Long ticketId);

    CheckoutResponseDto checkout(Long memberId, CheckoutRequestDto requestDto);
}
