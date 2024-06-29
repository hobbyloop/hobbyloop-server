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
import com.example.ticketservice.pay.dto.response.CheckoutPrepareResponseDto;
import com.example.ticketservice.pay.dto.response.CheckoutResponseDto;
import com.example.ticketservice.pay.dto.response.PaymentConfirmExecuteResponseDto;
import com.example.ticketservice.pay.dto.response.PaymentConfirmResponseDto;
import com.example.ticketservice.pay.entity.member.Checkout;
import com.example.ticketservice.pay.entity.member.Payment;
import com.example.ticketservice.pay.entity.member.PurchaseHistory;
import com.example.ticketservice.pay.entity.member.enums.PaymentStatusEnum;
import com.example.ticketservice.pay.entity.member.vo.PointUsage;
import com.example.ticketservice.pay.exception.PaymentAlreadyProcessedException;
import com.example.ticketservice.pay.repository.CheckoutRepository;
import com.example.ticketservice.pay.repository.PaymentRepository;
import com.example.ticketservice.pay.repository.purchasehistory.PurchaseHistoryRepository;
import com.example.ticketservice.pay.toss.PSPConfirmationException;
import com.example.ticketservice.pay.toss.TossPaymentClient;
import com.example.ticketservice.point.entity.Point;
import com.example.ticketservice.point.entity.enums.PointUsableScopeEnum;
import com.example.ticketservice.point.repository.PointRepository;
import com.example.ticketservice.point.service.PointService;
import com.example.ticketservice.ticket.client.CompanyServiceClient;
import com.example.ticketservice.ticket.entity.Ticket;
import com.example.ticketservice.ticket.repository.ticket.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final CheckoutRepository checkoutRepository;
    private final PurchaseHistoryRepository purchaseHistoryRepository;
    private final TicketRepository ticketRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final PointRepository pointRepository;
    private final CompanyServiceClient companyServiceClient;

    private final TossPaymentClient tossPaymentClient;

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
        // Member 마이크로서비스 조회도 해야됨.

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.TICKET_NOT_EXIST_EXCEPTION));

        String idempotencyKey = generateIdempotencyKey(memberId, ticketId);
        Checkout checkout = checkoutRepository.findByMemberIdAndTicketIdAndIdempotencyKey(memberId, ticketId, idempotencyKey)
                .orElseGet(() -> Checkout.prepare(memberId, ticket));

        Long companyId = companyServiceClient.getCompanyIdOfCenter(ticket.getCenterId()).getData();

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
        List<Point> points = pointRepository.findByMemberId(memberId);
        Long totalPoints = points.stream().mapToLong(Point::getBalance).sum();
        Long usablePoints = 0L;
        for (Point point : points) {
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

        Long usingPoints = requestDto.getPoints();
        List<PointUsage> pointUsages = new ArrayList<>();

        // 포인트 계산
        List<Point> points = pointRepository.findByMemberId(memberId);
        // 1. scope이 SPECIFIC_CENTER인 Point로, PointUsage를 만듦
        // 2. scope이 SPECIFIC_COMPANY인 Point로, PointUsage를 만듦
        // 실제 포인트 차감은 결제 완료 후.
        Point centerPoint = points.stream()
                .filter(point -> point.getUsableScope() == PointUsableScopeEnum.SPECIFIC_CENTER.getValue())
                .findFirst().orElse(null);
        if (centerPoint != null) {
            if (usingPoints <= centerPoint.getBalance()) {
                PointUsage pointUsage = new PointUsage(centerPoint.getId(), usingPoints);
                pointUsages.add(pointUsage);
                usingPoints = 0L;
            } else {
                PointUsage pointUsage = new PointUsage(centerPoint.getId(), centerPoint.getBalance());
                pointUsages.add(pointUsage);
                usingPoints -= centerPoint.getBalance();
            }
        }

        Point companyPoint = points.stream()
                .filter(point -> point.getUsableScope() == PointUsableScopeEnum.SPECIFIC_COMPANY.getValue())
                .findFirst().orElse(null);
        if (companyPoint != null && usingPoints > 0L) {
            if (usingPoints <= companyPoint.getBalance()) {
                PointUsage pointUsage = new PointUsage(companyPoint.getId(), usingPoints);
                pointUsages.add(pointUsage);
                usingPoints = 0L;
            } else {
                PointUsage pointUsage = new PointUsage(companyPoint.getId(), companyPoint.getBalance());
                pointUsages.add(pointUsage);
                usingPoints -= companyPoint.getBalance();
            }
        }

        Point generalPoint = points.stream()
                .filter(point -> point.getUsableScope() == PointUsableScopeEnum.GENERAL.getValue())
                .findFirst().orElse(null);
        if (generalPoint != null && usingPoints > 0L) {
            if (usingPoints <= generalPoint.getBalance()) {
                PointUsage pointUsage = new PointUsage(generalPoint.getId(), usingPoints);
                pointUsages.add(pointUsage);
                usingPoints = 0L;
            } else {
                PointUsage pointUsage = new PointUsage(generalPoint.getId(), generalPoint.getBalance());
                pointUsages.add(pointUsage);
                usingPoints -= generalPoint.getBalance();
            }
        }

        // usingPoints가 계산을 마친 후에도 0이 아니면 사용 가능한 포인트보다 더 많은 포인트를 요청한 것.
        if (usingPoints > 0L) {
            throw new ApiException(ExceptionEnum.API_PARAMETER_EXCEPTION);
        }

        if (!memberCouponRepository.existsById(requestDto.getMemberCouponId())) {
            throw new ApiException(ExceptionEnum.COUPON_NOT_EXIST_EXCEPTION);
        }

        checkout.checkout(requestDto, pointUsages);
        Payment payment = Payment.checkout(checkout, ticket);
        paymentRepository.save(payment);

        return CheckoutResponseDto.of(checkout, payment);
    }

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
            // e두 번째 인자가 previousStatus, 세번째 인자가 NewStatus, 마지막 인자가 updateReason
            PurchaseHistory executingHistory = PurchaseHistory.record(payment, PaymentStatusEnum.NOT_STARTED, PaymentStatusEnum.EXECUTING, "결제 승인 시작");
            purchaseHistoryRepository.save(executingHistory);
            payment.execute();

            response = tossPaymentClient.executeConfirm(requestDto)
                    .blockOptional()
                    .orElse(null);

            PurchaseHistory confirmedHistory = PurchaseHistory.record(payment, PaymentStatusEnum.findByValue(payment.getStatus()), PaymentStatusEnum.SUCCESS, "결제 승인 완료");
            purchaseHistoryRepository.save(confirmedHistory);
            payment.confirm(response);
        } catch (Exception ex) {
            PaymentStatusEnum paymentStatus;
            String errorCode;
            String errorMessage;
            if (ex instanceof PSPConfirmationException) {
                PSPConfirmationException exception = (PSPConfirmationException) ex;
                paymentStatus = exception.paymentStatus();
                errorCode = exception.getErrorCode();
                errorMessage = exception.getErrorMessage();
            } else if (ex instanceof PaymentAlreadyProcessedException) {
                PaymentAlreadyProcessedException exception = (PaymentAlreadyProcessedException) ex;
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

            return new PaymentConfirmResponseDto(paymentStatus.name(), errorCode, errorMessage);
        }

        return new PaymentConfirmResponseDto(PaymentStatusEnum.SUCCESS.name(), null, null);
    }

}
