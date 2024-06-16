package com.example.ticketservice.coupon.service;

import com.example.ticketservice.common.exception.ApiException;
import com.example.ticketservice.common.exception.ExceptionEnum;
import com.example.ticketservice.coupon.dto.CouponCreateRequestDto;
import com.example.ticketservice.coupon.dto.CouponResponseDto;
import com.example.ticketservice.coupon.entity.Coupon;
import com.example.ticketservice.coupon.entity.MemberCoupon;
import com.example.ticketservice.coupon.repository.CouponRepository;
import com.example.ticketservice.coupon.repository.MemberCouponRepository;
import com.example.ticketservice.ticket.client.CompanyServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    private final CouponRepository couponRepository;

    private final MemberCouponRepository memberCouponRepository;

    private final CompanyServiceClient companyServiceClient;

    @Override
    public Long createCoupon(CouponCreateRequestDto request) {
        Coupon coupon = request.toEntity();
        return couponRepository.save(coupon).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CouponResponseDto> getCenterCouponListForMember(Long memberId, Long centerId) {
        Long companyId = companyServiceClient.getCompanyIdOfCenter(centerId).getData();

        return couponRepository.findCenterCouponList(memberId, companyId, centerId).stream()
                .map(CouponResponseDto::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountOfAvailableMemberCoupons(Long memberId) {
        return memberCouponRepository.countByIsUsedFalseAndExpirationDateTimeAfter(LocalDateTime.now());
    }

    // TODO: findForUpdate로 바꾸던지 동시성 처리 해야됨
    @Override
    @Transactional
    public Long issueSingleCoupon(Long memberId, Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.COUPON_NOT_EXIST_EXCEPTION));

        coupon.checkForIssue();

        long issueCount = memberCouponRepository.countByMemberIdAndCoupon(memberId, coupon);
        if (issueCount >= coupon.getMaxIssuancePerMember()) {
            throw new ApiException(ExceptionEnum.COUPON_ALREADY_ISSUED_EXCEPTION);
        }

        MemberCoupon memberCoupon = MemberCoupon.issue(memberId, coupon);
        coupon.issue();

        return memberCouponRepository.save(memberCoupon).getId();
    }
}
