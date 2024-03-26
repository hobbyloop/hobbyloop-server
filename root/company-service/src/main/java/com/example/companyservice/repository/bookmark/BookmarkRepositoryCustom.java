package com.example.companyservice.repository.bookmark;

import com.example.companyservice.entity.Bookmark;

import java.util.List;

public interface BookmarkRepositoryCustom {

    List<Bookmark> getBookmarkList(long memberId, long bookmarkId, int sortId);
}
