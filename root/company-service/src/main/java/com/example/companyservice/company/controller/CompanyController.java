package com.example.companyservice.company.controller;

import com.example.companyservice.common.util.Utils;
import com.example.companyservice.company.dto.BaseResponseDto;
import com.example.companyservice.company.dto.request.CompanyCreateRequestDto;
import com.example.companyservice.company.service.CompanyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping("/join/companies")
    public ResponseEntity<BaseResponseDto<Long>> createCompany(@RequestBody @Valid CompanyCreateRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(companyService.createCompany(requestDto)));
    }

    @GetMapping("/admin/companies/check/tax-free")
    public ResponseEntity<BaseResponseDto<Boolean>> checkTaxFree(HttpServletRequest request) {
        long companyId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(companyService.checkTaxFree(companyId)));
    }
}
