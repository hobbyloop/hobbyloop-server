package com.example.ticketservice.coupon.service;

import com.example.ticketservice.common.exception.ApiException;
import com.example.ticketservice.common.exception.ExceptionEnum;
import com.example.ticketservice.coupon.dto.CouponCreateRequestDto;
import com.example.ticketservice.coupon.dto.CouponResponseDto;
import com.example.ticketservice.coupon.dto.MemberCouponResponseDto;
import com.example.ticketservice.coupon.entity.Coupon;
import com.example.ticketservice.coupon.entity.MemberCoupon;
import com.example.ticketservice.coupon.entity.vo.CenterInfo;
import com.example.ticketservice.coupon.entity.vo.CompanyInfo;
import com.example.ticketservice.coupon.repository.CouponRepository;
import com.example.ticketservice.coupon.repository.MemberCouponRepository;
import com.example.ticketservice.ticket.client.CompanyServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

        List<CompanyInfo> excludedCompanies = new ArrayList<>();
        if (!request.getExcludedCompanyIds().isEmpty()) {
            for (Long companyId: request.getExcludedCompanyIds()) {
                String companyName = companyServiceClient.getCompanyName(companyId).getData();
                excludedCompanies.add(new CompanyInfo(companyId, companyName));
            }
        }

        List<CenterInfo> excludedCenters = new ArrayList<>();
        if (!request.getExcludedCenterIds().isEmpty()) {
            for (Long centerId : request.getExcludedCenterIds()) {
                String centerName = companyServiceClient.getCenterInfo(centerId).getData().getCenterName();
                excludedCenters.add(new CenterInfo(centerId, centerName));
            }
        }

        Coupon coupon = request.toEntity(excludedCompanies, excludedCenters);

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
        return memberCouponRepository.countByMemberIdAndIsUsedFalseAndExpirationDateTimeAfter(memberId, LocalDateTime.now());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberCouponResponseDto> getAvailableMemberCoupons(Long memberId) {
        return memberCouponRepository.findAllByMemberIdAndIsUsedFalseAndExpirationDateTimeAfter(memberId, LocalDateTime.now())
                .stream()
                .map(MemberCouponResponseDto::from)
                .collect(Collectors.toList());
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

    @Override
    @Transactional
    public void issueAllCoupons(Long memberId, List<Long> couponIds) {
        couponIds.forEach(
                couponId -> issueSingleCoupon(memberId, couponId)
        );
    }
}
