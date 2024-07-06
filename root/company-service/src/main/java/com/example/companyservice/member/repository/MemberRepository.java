package com.example.companyservice.member.repository;

import com.example.companyservice.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByProviderAndSubjectAndIsDeletedFalse(String provider, String subject);

    // Test
    Optional<Member> findByEmailAndProviderAndSubject(String email, String provider, String subject);

    boolean existsByProviderAndSubjectAndIsDeletedFalse(String provider, String subject);

    Optional<Member> findByIdAndIsDeletedFalse(Long id);
}
