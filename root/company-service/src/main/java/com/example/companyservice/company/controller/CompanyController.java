package com.example.companyservice.company.controller;

import com.example.companyservice.company.common.util.Utils;
import com.example.companyservice.company.dto.BaseResponseDto;
import com.example.companyservice.company.dto.request.CompanyUpdateRequestDto;
import com.example.companyservice.company.service.CompanyService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CompanyController {

    private final CompanyService companyService;

    @PatchMapping("/admin/companies")
    public ResponseEntity<BaseResponseDto<Long>> updateCompanyInfo(HttpServletRequest request,
                                                                   @RequestBody CompanyUpdateRequestDto requestDto) {
        long companyId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(companyService.updateCompanyInfo(companyId, requestDto)));
    }

    @GetMapping("/admin/companies/check/tax-free")
    public ResponseEntity<BaseResponseDto<Boolean>> checkTaxFree(HttpServletRequest request) {
        long companyId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(companyService.checkTaxFree(companyId)));
    }
}