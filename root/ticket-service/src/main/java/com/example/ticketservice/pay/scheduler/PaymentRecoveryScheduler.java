package com.example.ticketservice.pay.scheduler;

import com.example.ticketservice.pay.dto.request.PaymentConfirmRequestDto;
import com.example.ticketservice.pay.dto.response.PaymentConfirmExecuteResponseDto;
import com.example.ticketservice.pay.entity.member.Payment;
import com.example.ticketservice.pay.entity.member.PurchaseHistory;
import com.example.ticketservice.pay.entity.member.enums.PaymentStatusEnum;
import com.example.ticketservice.pay.event.PaymentCompletedEvent;
import com.example.ticketservice.pay.exception.PaymentAlreadyProcessedException;
import com.example.ticketservice.pay.repository.PaymentRepository;
import com.example.ticketservice.pay.repository.purchasehistory.PurchaseHistoryRepository;
import com.example.ticketservice.pay.toss.PSPConfirmationException;
import com.example.ticketservice.pay.toss.TossPaymentClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Component
@RequiredArgsConstructor
public class PaymentRecoveryScheduler {
    private final PaymentRepository paymentRepository;
    private final PurchaseHistoryRepository purchaseHistoryRepository;
    private final TossPaymentClient tossPaymentClient;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void recover() {
        List<Payment> payments = paymentRepository.findPendingPayments(LocalDateTime.now());

        for (Payment payment : payments) {
            PaymentConfirmRequestDto request = new PaymentConfirmRequestDto(
                    payment.getId(),
                    payment.getPspPaymentKey(),
                    payment.getIdempotencyKey(),
                    payment.getAmount(),
                    payment.getPsp()
            );

            PaymentConfirmExecuteResponseDto response;
            try {
                PurchaseHistory executingHistory = PurchaseHistory.record(payment, PaymentStatusEnum.NOT_STARTED, PaymentStatusEnum.EXECUTING, "결제 승인 시작");
                purchaseHistoryRepository.save(executingHistory);

                response = tossPaymentClient.executeConfirm(request)
                        .blockOptional()
                        .orElse(null);

                PurchaseHistory confirmedHistory = PurchaseHistory.record(payment, PaymentStatusEnum.findByValue(payment.getStatus()), PaymentStatusEnum.SUCCESS, "결제 승인 완료");
                purchaseHistoryRepository.save(confirmedHistory);
                payment.confirm(response);
            } catch (Exception ex) {
                PaymentStatusEnum paymentStatus;
                String errorCode;
                String errorMessage;
                if (ex instanceof PSPConfirmationException exception) {
                    paymentStatus = exception.paymentStatus();
                    errorCode = exception.getErrorCode();
                    errorMessage = exception.getErrorMessage();
                } else if (ex instanceof PaymentAlreadyProcessedException exception) {
                    paymentStatus = exception.getStatus();
                    errorCode = exception.getClass().getSimpleName();
                    errorMessage = exception.getMessage();
                } else if (ex instanceof TimeoutException) {
                    paymentStatus = PaymentStatusEnum.UNKNOWN;
                    errorCode = ex.getClass().getSimpleName();
                    errorMessage = ex.getMessage();
                } else {
                    paymentStatus = PaymentStatusEnum.UNKNOWN;
                    errorCode = ex.getClass().getSimpleName();
                    errorMessage = ex.getMessage();
                }

                PurchaseHistory failedHistory = PurchaseHistory.record(payment, PaymentStatusEnum.findByValue(payment.getStatus()), paymentStatus, errorMessage);
                purchaseHistoryRepository.save(failedHistory);
                payment.failOrUnknown(paymentStatus, errorCode, errorMessage);
            }

            eventPublisher.publishEvent(new PaymentCompletedEvent(payment));
        }
    }
}
