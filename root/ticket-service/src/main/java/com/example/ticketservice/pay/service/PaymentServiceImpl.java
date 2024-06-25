package com.example.ticketservice.pay.service;

import com.example.ticketservice.common.exception.ApiException;
import com.example.ticketservice.common.exception.ExceptionEnum;
import com.example.ticketservice.coupon.dto.MemberCouponResponseDto;
import com.example.ticketservice.coupon.entity.MemberCoupon;
import com.example.ticketservice.coupon.entity.vo.CenterInfo;
import com.example.ticketservice.coupon.entity.vo.CompanyInfo;
import com.example.ticketservice.coupon.repository.MemberCouponRepository;
import com.example.ticketservice.pay.dto.response.CheckoutPrepareResponseDto;
import com.example.ticketservice.pay.entity.member.Checkout;
import com.example.ticketservice.pay.repository.CheckoutRepository;
import com.example.ticketservice.pay.repository.PaymentRepository;
import com.example.ticketservice.point.repository.PointRepository;
import com.example.ticketservice.ticket.client.CompanyServiceClient;
import com.example.ticketservice.ticket.entity.Ticket;
import com.example.ticketservice.ticket.repository.ticket.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl {
    private final PaymentRepository paymentRepository;
    private final CheckoutRepository checkoutRepository;
    private final TicketRepository ticketRepository;
    private final MemberCouponRepository memberCouponRepository;
    private final PointRepository pointRepository;
    private final CompanyServiceClient companyServiceClient;

    public CheckoutPrepareResponseDto prepareCheckout(Long memberId, Long ticketId) {
        // 사용 가능 쿠폰 목록
        // 전체 포인트 + 그 중 사용 가능 포인트 잔액
        // Member 마이크로서비스 조회도 해야됨.

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.TICKET_NOT_EXIST_EXCEPTION));

        Checkout checkout = Checkout.prepare(memberId, ticket);

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

            Long companyId = companyServiceClient.getCompanyIdOfCenter(ticket.getCenterId()).getData();

//            if (!excludedCompanies.contains(companyId)
//                && !excludedCenters.contains(ticket.getCenterId())
//                && (coupon.getCoupon().isGeneralScope() || coupon.getCoupon().isSpecificCompanyScope()))
        }

        return null;
    }

}
