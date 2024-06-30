package com.example.companyservice.member.repository;

import com.example.companyservice.member.entity.MemberArchive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberArchiveRepository extends JpaRepository<MemberArchive, Long> {
}
