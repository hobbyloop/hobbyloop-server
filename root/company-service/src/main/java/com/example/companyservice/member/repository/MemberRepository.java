package com.example.companyservice.member.repository;

import com.example.companyservice.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByProviderAndSubject(String provider, String subject);

    boolean existsByProviderAndSubject(String provider, String subject);

    Optional<Member> findByIdAndIsDeletedFalse(Long id);
}
