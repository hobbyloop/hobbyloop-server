package com.example.companyservice.company.controller;

import com.example.companyservice.common.dto.BaseResponseDto;
import com.example.companyservice.company.service.CompanyService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/companies/client")
@Hidden
public class CompanyClientController {
    private final CompanyService companyService;

    @GetMapping("/name/{companyId}")
    public ResponseEntity<BaseResponseDto<String>> getCompanyName(@PathVariable(value = "companyId") long companyId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(companyService.getCompanyName(companyId)));
    }
}
