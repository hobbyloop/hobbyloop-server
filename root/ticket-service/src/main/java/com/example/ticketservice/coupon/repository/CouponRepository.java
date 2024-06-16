package com.example.ticketservice.coupon.repository;

import com.example.ticketservice.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long>, CouponRepositoryCustom {


}
