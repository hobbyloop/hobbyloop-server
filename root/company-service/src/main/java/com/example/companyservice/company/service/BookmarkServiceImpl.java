package com.example.companyservice.company.service;

import com.example.companyservice.common.exception.ApiException;
import com.example.companyservice.common.exception.ExceptionEnum;
import com.example.companyservice.company.entity.Bookmark;
import com.example.companyservice.company.entity.Center;
import com.example.companyservice.company.repository.bookmark.BookmarkRepository;
import com.example.companyservice.company.repository.CenterRepository;
import com.example.companyservice.member.entity.Member;
import com.example.companyservice.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

    private final CenterRepository centerRepository;

    private final BookmarkRepository bookmarkRepository;

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Long createBookmark(long memberId, long centerId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.MEMBER_NOT_EXIST_EXCEPTION));
        Center center = centerRepository.findById(centerId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.CENTER_NOT_EXIST_EXCEPTION));
        Optional<Bookmark> bookmarkOptional = bookmarkRepository.findByCenterIdAndMemberId(centerId, memberId);
        if (bookmarkOptional.isPresent()) throw new ApiException(ExceptionEnum.BOOKMARK_ALREADY_EXIST_EXCEPTION);
        Bookmark bookmark = Bookmark.of(center, member);
        bookmarkRepository.save(bookmark);
        return bookmark.getId();
    }

    @Override
    @Transactional
    public void deleteBookmark(long memberId, long centerId) {
        Bookmark bookmark = bookmarkRepository.findByCenterIdAndMemberId(centerId, memberId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.BOOKMARK_ALREADY_DELETE_EXCEPTION));
        bookmarkRepository.delete(bookmark);
    }
}
