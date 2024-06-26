package com.example.ticketservice.coupon.repository;

import com.example.ticketservice.coupon.entity.Coupon;
import com.example.ticketservice.coupon.entity.MemberCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long> {
    List<MemberCoupon> findAllByMemberId(Long memberId);

    // 회원이 보유한 사용 가능 쿠폰 조회
    List<MemberCoupon> findAllByMemberIdAndIsUsedFalseAndExpirationDateTimeAfter(Long memberId, LocalDateTime now);

    // 사용 가능한 쿠폰 개수 조회
    long countByMemberIdAndIsUsedFalseAndExpirationDateTimeAfter(Long memberId, LocalDateTime now);

    // 최대 발급 횟수만큼 발급받은 쿠폰 여부 조회
    long countByMemberIdAndCoupon(Long memberId, Coupon coupon);
}
