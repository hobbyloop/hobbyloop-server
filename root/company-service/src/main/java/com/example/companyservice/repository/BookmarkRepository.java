package com.example.companyservice.repository;

import com.example.companyservice.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Optional<Bookmark> findByCenterIdAndMemberId(long centerId, long memberId);

    List<Bookmark> findAllByMemberId(long memberId);
}
