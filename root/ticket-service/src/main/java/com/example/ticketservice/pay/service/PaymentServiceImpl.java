package com.example.ticketservice.pay.service;

import com.example.ticketservice.common.exception.ApiException;
import com.example.ticketservice.common.exception.ExceptionEnum;
import com.example.ticketservice.coupon.dto.MemberCouponResponseDto;
import com.example.ticketservice.coupon.entity.MemberCoupon;
import com.example.ticketservice.coupon.entity.vo.CenterInfo;
import com.example.ticketservice.coupon.entity.vo.CompanyInfo;
import com.example.ticketservice.coupon.repository.MemberCouponRepository;
import com.example.ticketservice.pay.dto.request.CheckoutRequestDto;
import com.example.ticketservice.pay.dto.request.PaymentConfirmRequestDto;
import com.example.ticketservice.pay.dto.response.*;
import com.example.ticketservice.pay.entity.member.Checkout;
import com.example.ticketservice.pay.entity.member.Payment;
import com.example.ticketservice.pay.entity.member.PaymentRefund;
import com.example.ticketservice.pay.entity.member.PurchaseHistory;
import com.example.ticketservice.pay.entity.member.enums.PaymentStatusEnum;
import com.example.ticketservice.pay.entity.member.vo.PointUsage;
import com.example.ticketservice.pay.event.PaymentCompletedEvent;
import com.example.ticketservice.pay.event.PaymentRefundedEvent;
import com.example.ticketservice.pay.exception.PaymentAlreadyProcessedException;
import com.example.ticketservice.pay.repository.CheckoutRepository;
import com.example.ticketservice.pay.repository.PaymentRefundRepository;
import com.example.ticketservice.pay.repository.payment.PaymentRepository;
import com.example.ticketservice.pay.repository.purchasehistory.PurchaseHistoryRepository;
import com.example.ticketservice.pay.exception.PSPConfirmationException;
import com.example.ticketservice.pay.toss.TossPaymentClient;
import com.example.ticketservice.point.entity.Points;
import com.example.ticketservice.point.entity.enums.PointUsableScopeEnum;
import com.example.ticketservice.point.repository.PointsRepository;
import com.example.ticketservice.ticket.client.CompanyServiceClient;
import com.example.ticketservice.ticket.client.dto.response.MemberInfoResponseDto;
import com.example.ticketservice.ticket.entity.Ticket;
import com.example.ticketservice.ticket.entity.UserTicket;
import com.example.ticketservice.ticket.repository.ticket.TicketRepository;
import com.example.ticketservice.ticket.repository.ticket.UserTicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final CheckoutRepository checkoutRepository;
    private final PurchaseHistoryRepository purchaseHistoryRepository;
    private final TicketRepository ticketRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final PointsRepository pointsRepository;
    private final CompanyServiceClient companyServiceClient;
    private final TossPaymentClient tossPaymentClient;
    private final ApplicationEventPublisher eventPublisher;
    private final PaymentRefundRepository paymentRefundRepository;

    private String generateIdempotencyKey(Long memberId, Long ticketId) {
        LocalDateTime now = LocalDateTime.now();
        long timestamp = now.toEpochSecond(ZoneOffset.UTC);
        String seed = String.valueOf(Math.floorDiv(timestamp, 3)); // 3초 내 중복 요청 비허용
        return UUID.nameUUIDFromBytes((memberId + ticketId + seed).getBytes()).toString();
    }

    @Override
    public CheckoutPrepareResponseDto prepareCheckout(Long memberId, Long ticketId) {
        // 사용 가능 쿠폰 목록
        // 전체 포인트 + 그 중 사용 가능 포인트 잔액

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.TICKET_NOT_EXIST_EXCEPTION));

        String idempotencyKey = generateIdempotencyKey(memberId, ticketId);
        Checkout checkout = checkoutRepository.findByMemberIdAndTicketIdAndIdempotencyKey(memberId, ticketId, idempotencyKey)
                .orElseGet(() -> Checkout.prepare(memberId, ticket));

        Long companyId = companyServiceClient.getCompanyIdOfCenter(ticket.getCenterId()).getData();

        // 현재 이용권 구매 가능한지 확인
        ticket.checkCanPurchase();

        // 쿠폰 조회
        // 쿠폰의 scope을 확인함
        List<MemberCoupon> coupons = memberCouponRepository.findAllByMemberIdAndIsUsedFalseAndExpirationDateTimeAfter(memberId, LocalDateTime.now());
        List<MemberCouponResponseDto> couponResponses = new ArrayList<>();
        for (MemberCoupon coupon : coupons) {
            List<Long> excludedCompanies = coupon.getCoupon().getExcludedCompanies().stream()
                    .map(CompanyInfo::getId)
                    .collect(Collectors.toList());
            List<Long> excludedCenters = coupon.getCoupon().getExcludedCenters().stream()
                    .map(CenterInfo::getId)
                    .collect(Collectors.toList());


            // 쿠폰 사용 가능 여부 검증
            if (!excludedCompanies.contains(companyId)
                && !excludedCenters.contains(ticket.getCenterId())
                && (coupon.getCoupon().isGeneralScope()
                    || (coupon.getCoupon().isSpecificCompanyScope() && Objects.equals(coupon.getCoupon().getCompanyId(), companyId))
                    || (coupon.getCoupon().isSpecificCenterScope() && Objects.equals(coupon.getCoupon().getCenterId(), ticket.getCenterId())))) {
                couponResponses.add(MemberCouponResponseDto.from(coupon));
            }
        }

        // 포인트 조회
        List<Points> points = pointsRepository.findByMemberId(memberId);
        Long totalPoints = points.stream().mapToLong(Points::getBalance).sum();
        Long usablePoints = 0L;
        for (Points point : points) {
            if (point.getUsableScope() == PointUsableScopeEnum.GENERAL.getValue()
                || (point.getUsableScope() == PointUsableScopeEnum.SPECIFIC_COMPANY.getValue() && point.getCompanyId() == companyId)
                || (point.getUsableScope() == PointUsableScopeEnum.SPECIFIC_CENTER.getValue() && point.getCenterId() == ticket.getCenterId())) {
                usablePoints += point.getBalance();
            }
        }

        checkoutRepository.save(checkout);

        return CheckoutPrepareResponseDto.of(checkout, couponResponses, totalPoints, usablePoints, ticket);
    }

    @Override
    @Transactional
    public CheckoutResponseDto checkout(Long memberId, CheckoutRequestDto requestDto) {
        Checkout checkout = checkoutRepository.findById(requestDto.getCheckoutId())
                .orElseThrow(() -> new ApiException(ExceptionEnum.CHECKOUT_NOT_EXIST_EXCEPTION));
        Ticket ticket = ticketRepository.findById(checkout.getTicketId())
                .orElseThrow(() -> new ApiException(ExceptionEnum.TICKET_NOT_EXIST_EXCEPTION));

        MemberInfoResponseDto memberInfo = companyServiceClient.getMemberInfo(memberId).getData();

        Long usingPoints = requestDto.getPoints();
        List<PointUsage> pointUsages = new ArrayList<>();

        // 포인트 계산
        List<Points> points = pointsRepository.findByMemberId(memberId);
        // 1. scope이 SPECIFIC_CENTER인 Point로, PointUsage를 만듦
        // 2. scope이 SPECIFIC_COMPANY인 Point로, PointUsage를 만듦
        // 실제 포인트 차감은 결제 완료 후.
        Points centerPoints = points.stream()
                .filter(point -> point.getUsableScope() == PointUsableScopeEnum.SPECIFIC_CENTER.getValue())
                .findFirst().orElse(null);
        if (centerPoints != null) {
            if (usingPoints <= centerPoints.getBalance()) {
                PointUsage pointUsage = new PointUsage(centerPoints.getId(), usingPoints);
                pointUsages.add(pointUsage);
                usingPoints = 0L;
            } else {
                PointUsage pointUsage = new PointUsage(centerPoints.getId(), centerPoints.getBalance());
                pointUsages.add(pointUsage);
                usingPoints -= centerPoints.getBalance();
            }
        }

        Points companyPoints = points.stream()
                .filter(point -> point.getUsableScope() == PointUsableScopeEnum.SPECIFIC_COMPANY.getValue())
                .findFirst().orElse(null);
        if (companyPoints != null && usingPoints > 0L) {
            if (usingPoints <= companyPoints.getBalance()) {
                PointUsage pointUsage = new PointUsage(companyPoints.getId(), usingPoints);
                pointUsages.add(pointUsage);
                usingPoints = 0L;
            } else {
                PointUsage pointUsage = new PointUsage(companyPoints.getId(), companyPoints.getBalance());
                pointUsages.add(pointUsage);
                usingPoints -= companyPoints.getBalance();
            }
        }

        Points generalPoints = points.stream()
                .filter(point -> point.getUsableScope() == PointUsableScopeEnum.GENERAL.getValue())
                .findFirst().orElse(null);
        if (generalPoints != null && usingPoints > 0L) {
            if (usingPoints <= generalPoints.getBalance()) {
                PointUsage pointUsage = new PointUsage(generalPoints.getId(), usingPoints);
                pointUsages.add(pointUsage);
                usingPoints = 0L;
            } else {
                PointUsage pointUsage = new PointUsage(generalPoints.getId(), generalPoints.getBalance());
                pointUsages.add(pointUsage);
                usingPoints -= generalPoints.getBalance();
            }
        }

        // usingPoints가 계산을 마친 후에도 0이 아니면 사용 가능한 포인트보다 더 많은 포인트를 요청한 것.
        if (usingPoints > 0L) {
            throw new ApiException(ExceptionEnum.API_PARAMETER_EXCEPTION); // TODO: Exception 정의
        }

        if (!memberCouponRepository.existsById(requestDto.getMemberCouponId())) {
            throw new ApiException(ExceptionEnum.COUPON_NOT_EXIST_EXCEPTION);
        }

        checkout.checkout(requestDto, pointUsages);
        Payment payment = Payment.checkout(checkout, ticket, memberInfo);
        paymentRepository.save(payment);

        return CheckoutResponseDto.of(checkout, payment);
    }

    // TODO: 결제 완료 이후 할일: Ledger, Wallet에 기록
    @Override
    @Transactional
    public PaymentConfirmResponseDto confirm(Long memberId, PaymentConfirmRequestDto requestDto) {
        Payment payment = paymentRepository.findByIdAndIdempotencyKey(requestDto.getPaymentId(), requestDto.getIdempotencyKey())
                .orElseThrow(() -> new ApiException(ExceptionEnum.PAYMENT_NOT_EXIST_EXCEPTION));

        // validation
        if (!payment.getMemberId().equals(memberId)) {
            throw new ApiException(ExceptionEnum.UNAUTHORIZED_PAYMENT_REQUEST_EXCEPTION);
        }
        if (!Objects.equals(payment.getAmount(), requestDto.getAmount())) {
            throw new ApiException(ExceptionEnum.PAYMENT_AMOUNT_MISMATCH_EXCEPTION);
        }

        PaymentConfirmExecuteResponseDto response;
        try {
            // ---
            PurchaseHistory executingHistory = PurchaseHistory.record(payment, PaymentStatusEnum.NOT_STARTED, PaymentStatusEnum.EXECUTING, "결제 승인 시작");
            purchaseHistoryRepository.save(executingHistory);
            payment.execute(requestDto.getPsp(), requestDto.getPaymentKey());
            // ---

            response = tossPaymentClient.executeConfirm(requestDto)
                    .blockOptional()
                    .orElse(null);

            // ---
            PurchaseHistory confirmedHistory = PurchaseHistory.record(payment, PaymentStatusEnum.findByValue(payment.getStatus()), PaymentStatusEnum.SUCCESS, "결제 승인 완료");
            purchaseHistoryRepository.save(confirmedHistory);
            payment.confirm(response);
            // ---
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

            // ---
            PurchaseHistory failedHistory = PurchaseHistory.record(payment, PaymentStatusEnum.findByValue(payment.getStatus()), paymentStatus, errorMessage);
            purchaseHistoryRepository.save(failedHistory);
            payment.failOrUnknown(paymentStatus, errorCode, errorMessage);
            // ---

            return new PaymentConfirmResponseDto(paymentStatus.name(), errorCode, errorMessage);
        }

        eventPublisher.publishEvent(new PaymentCompletedEvent(payment));

        return new PaymentConfirmResponseDto(PaymentStatusEnum.SUCCESS.name(), "", "");
    }

    @Override
    @Transactional
    public PaymentConfirmResponseDto refund(Long memberId, Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.PAYMENT_NOT_EXIST_EXCEPTION));

        // validation
        if (!payment.getMemberId().equals(memberId)) {
            throw new ApiException(ExceptionEnum.UNAUTHORIZED_PAYMENT_REQUEST_EXCEPTION);
        }

        Long refundAmount = payment.getAmount();
        if (payment.getUserTicket() != null) {
            refundAmount = RefundAmountCalculator.calculate(payment.getUserTicket(), payment.getAmount(), LocalDate.now());
        }
        PaymentRefund refund = PaymentRefund.of(payment, refundAmount);
        paymentRefundRepository.save(refund);

        PaymentConfirmExecuteResponseDto response;
        try {
            PurchaseHistory executingHistory = PurchaseHistory.record(refund, PaymentStatusEnum.findByValue(payment.getStatus()), PaymentStatusEnum.EXECUTING, "결제 취소 시작");
            purchaseHistoryRepository.save(executingHistory);
            refund.execute();

            response = tossPaymentClient.executeFullCancel(payment.getPspPaymentKey(), payment.getIdempotencyKey(), "고객이 취소를 원함")
                    .blockOptional()
                    .orElse(null);

            refund.refund(response);
        } catch (Exception ex) {
            PaymentStatusEnum refundStatus;
            String errorCode;
            String errorMessage;
            if (ex instanceof PSPConfirmationException exception) {
                refundStatus = exception.paymentStatus();
                errorCode = exception.getErrorCode();
                errorMessage = exception.getErrorMessage();
            } else if (ex instanceof PaymentAlreadyProcessedException) {
                PaymentAlreadyProcessedException exception = (PaymentAlreadyProcessedException) ex;
                refundStatus = exception.getStatus();
                errorCode = exception.getClass().getSimpleName();
                errorMessage = exception.getMessage();
            } else if (ex instanceof TimeoutException) {
                refundStatus = PaymentStatusEnum.UNKNOWN;
                errorCode = ex.getClass().getSimpleName();
                errorMessage = ex.getMessage();
            } else {
                refundStatus = PaymentStatusEnum.UNKNOWN;
                errorCode = ex.getClass().getSimpleName();
                errorMessage = ex.getMessage();
            }

            PurchaseHistory failedHistory = PurchaseHistory.record(refund, PaymentStatusEnum.findByValue(refund.getStatus()), refundStatus, errorMessage);
            purchaseHistoryRepository.save(failedHistory);
            refund.failOrUnknown(refundStatus, errorCode, errorMessage);

            return new PaymentConfirmResponseDto(refundStatus.name(), errorCode, errorMessage);
        }

        eventPublisher.publishEvent(new PaymentRefundedEvent(refund));

        return new PaymentConfirmResponseDto(PaymentStatusEnum.SUCCESS.name(), "", "");
    }

    // 관리자의 이용권 거절에 의한 환불은 무조건 전액환불
    public PaymentConfirmResponseDto refundByAdmin(Long adminId, UserTicket userTicket) {
        Payment payment = paymentRepository.findByUserTicket(userTicket)
                .orElseThrow(() -> new ApiException(ExceptionEnum.PAYMENT_NOT_EXIST_EXCEPTION));

        PaymentRefund refund = PaymentRefund.of(payment, payment.getAmount());

        PaymentConfirmExecuteResponseDto response;
        try {
            PurchaseHistory executingHistory = PurchaseHistory.record(refund, PaymentStatusEnum.findByValue(payment.getStatus()), PaymentStatusEnum.EXECUTING, "결제 취소 시작");
            purchaseHistoryRepository.save(executingHistory);
            refund.execute();

            response = tossPaymentClient.executeFullCancel(payment.getPspPaymentKey(), payment.getIdempotencyKey(), "판매자의 거절")
                    .blockOptional()
                    .orElse(null);

            refund.refund(response);
        } catch (Exception ex) {
            PaymentStatusEnum refundStatus;
            String errorCode;
            String errorMessage;
            if (ex instanceof PSPConfirmationException exception) {
                refundStatus = exception.paymentStatus();
                errorCode = exception.getErrorCode();
                errorMessage = exception.getErrorMessage();
            } else if (ex instanceof PaymentAlreadyProcessedException) {
                PaymentAlreadyProcessedException exception = (PaymentAlreadyProcessedException) ex;
                refundStatus = exception.getStatus();
                errorCode = exception.getClass().getSimpleName();
                errorMessage = exception.getMessage();
            } else if (ex instanceof TimeoutException) {
                refundStatus = PaymentStatusEnum.UNKNOWN;
                errorCode = ex.getClass().getSimpleName();
                errorMessage = ex.getMessage();
            } else {
                refundStatus = PaymentStatusEnum.UNKNOWN;
                errorCode = ex.getClass().getSimpleName();
                errorMessage = ex.getMessage();
            }

            PurchaseHistory failedHistory = PurchaseHistory.record(refund, PaymentStatusEnum.findByValue(refund.getStatus()), refundStatus, errorMessage);
            purchaseHistoryRepository.save(failedHistory);
            refund.failOrUnknown(refundStatus, errorCode, errorMessage);

            return new PaymentConfirmResponseDto(refundStatus.name(), errorCode, errorMessage);
        }

        eventPublisher.publishEvent(new PaymentRefundedEvent(refund));

        return new PaymentConfirmResponseDto(PaymentStatusEnum.SUCCESS.name(), "", "");
    }

//    public PaymentDetailResponseDto getPaymentDetail(Long paymentId) {
//
//    }

}
