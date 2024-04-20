package com.example.companyservice.company.repository.bookmark;

import com.example.companyservice.company.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long>, BookmarkRepositoryCustom {

    Optional<Bookmark> findByCenterIdAndMemberId(long centerId, long memberId);

    boolean existsByCenterIdAndMemberId(long centerId, long memberId);
}
