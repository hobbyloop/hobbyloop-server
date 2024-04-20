package com.example.companyservice.company.repository.bookmark;

import com.example.companyservice.company.entity.Bookmark;

import java.util.List;

public interface BookmarkRepositoryCustom {

    List<Bookmark> getBookmarkList(long memberId, long bookmarkId, int sortId);
}
