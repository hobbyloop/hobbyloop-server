package com.example.companyservice.company.controller;

import com.example.companyservice.common.dto.TokenResponseDto;
import com.example.companyservice.common.util.Utils;
import com.example.companyservice.common.dto.BaseResponseDto;
import com.example.companyservice.company.dto.request.CompanyCreateRequestDto;
import com.example.companyservice.company.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "업체 API")
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping("/join/companies")
    @Operation(summary = "업체 등록")
    @ApiResponse(responseCode = "201", description = "성공", content = @Content(schema = @Schema(implementation = TokenResponseDto.class)))
    public ResponseEntity<BaseResponseDto<TokenResponseDto>> createCompany(@RequestBody @Valid CompanyCreateRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseDto<>(companyService.createCompany(requestDto)));
    }

    @GetMapping("/admin/companies/check/tax-free")
    @Operation(summary = "면세 사업자 여부 체크")
    @ApiResponse(responseCode = "201", description = "성공", content = @Content(schema = @Schema(implementation = Boolean.class)))
    public ResponseEntity<BaseResponseDto<Boolean>> checkTaxFree(HttpServletRequest request) {
        long companyId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(companyService.checkTaxFree(companyId)));
    }
}
