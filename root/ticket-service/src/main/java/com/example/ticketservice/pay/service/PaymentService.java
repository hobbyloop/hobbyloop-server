package com.example.ticketservice.pay.service;

import com.example.ticketservice.pay.dto.request.CheckoutRequestDto;
import com.example.ticketservice.pay.dto.request.PaymentConfirmRequestDto;
import com.example.ticketservice.pay.dto.response.CheckoutPrepareResponseDto;
import com.example.ticketservice.pay.dto.response.CheckoutResponseDto;
import com.example.ticketservice.pay.dto.response.PaymentConfirmResponseDto;
import com.example.ticketservice.ticket.entity.UserTicket;

public interface PaymentService {
    CheckoutPrepareResponseDto prepareCheckout(Long memberId, Long ticketId);

    CheckoutResponseDto checkout(Long memberId, CheckoutRequestDto requestDto);

    PaymentConfirmResponseDto confirm(Long memberId, PaymentConfirmRequestDto requestDto);

    PaymentConfirmResponseDto refund(Long memberId, Long paymentId);

    PaymentConfirmResponseDto refundByAdmin(Long adminId, UserTicket userTicket);
}
