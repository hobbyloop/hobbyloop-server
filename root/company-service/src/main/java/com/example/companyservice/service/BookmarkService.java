package com.example.companyservice.service;

public interface BookmarkService {

    Long createBookmark(long memberId, long centerId);

    void deleteBookmark(long memberId, long centerId);
}
