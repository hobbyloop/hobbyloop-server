package com.example.companyservice.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyApplyResponseDto {

    private Long id;

    private int createStatus;

    private String representativeName;

    private String businessNumber;

    private String businessAddress;
}
