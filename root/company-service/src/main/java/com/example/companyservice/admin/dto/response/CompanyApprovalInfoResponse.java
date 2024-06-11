package com.example.companyservice.admin.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CompanyApprovalInfoResponse {

    private Long id;

    private int createStatus;

    private String representativeName;

    private String businessNumber;

    private String businessAddress;
}
