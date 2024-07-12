package com.example.companyservice.company.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookmarkCenterDto {

    private long bookmarkId;

    private long centerId;

    private String centerName;

    private boolean isRefundable;

    private String address;

    private String logoImageUrl;
}
