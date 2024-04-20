package com.example.companyservice.company.service;

import com.example.companyservice.company.common.exception.ApiException;
import com.example.companyservice.company.common.exception.ExceptionEnum;
import com.example.companyservice.company.entity.Bookmark;
import com.example.companyservice.company.entity.Center;
import com.example.companyservice.company.repository.bookmark.BookmarkRepository;
import com.example.companyservice.company.repository.CenterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

    private final CenterRepository centerRepository;

    private final BookmarkRepository bookmarkRepository;

    @Override
    @Transactional
    public Long createBookmark(long memberId, long centerId) {
        Center center = centerRepository.findById(centerId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.CENTER_NOT_EXIST_EXCEPTION));
        Optional<Bookmark> bookmarkOptional = bookmarkRepository.findByCenterIdAndMemberId(centerId, memberId);
        if (bookmarkOptional.isPresent()) throw new ApiException(ExceptionEnum.BOOKMARK_ALREADY_EXIST_EXCEPTION);
        Bookmark bookmark = Bookmark.of(center, memberId);
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
