package com.example.companyservice.company.repository.bookmark;

import com.example.companyservice.company.dto.response.BookmarkCenterDto;
import com.example.companyservice.company.entity.Bookmark;

import java.util.List;

public interface BookmarkRepositoryCustom {

    List<BookmarkCenterDto> getBookmarkList2(long memberId, long bookmarkId, int sortId);
}
