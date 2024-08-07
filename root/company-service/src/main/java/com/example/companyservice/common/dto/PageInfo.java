package com.example.companyservice.common.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PageInfo {

    private int currentPage;
    private int lastPage;
    private int countPerPage;
    private int totalSize;

    public static PageInfo from(Page<?> page) {
        return new PageInfo(page.getNumber() + 1, page.getTotalPages(), page.getSize(), (int) page.getTotalElements());
    }
    public static PageInfo from(int currentPage, int lastPage, int countPerPage, Long totalSize) {
        return new PageInfo(currentPage + 1, lastPage, countPerPage, totalSize.intValue());
    }

    public static PageInfo from(int currentPage, int lastPage, int countPerPage, int totalSize) {
        return new PageInfo(currentPage + 1, lastPage, countPerPage, totalSize);
    }
}
