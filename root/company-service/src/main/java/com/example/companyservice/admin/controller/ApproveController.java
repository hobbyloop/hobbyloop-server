package com.example.companyservice.admin.controller;

import com.example.companyservice.admin.dto.response.CompanyApplyResponseDto;
import com.example.companyservice.admin.service.ApproveService;
import com.example.companyservice.common.dto.BaseResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/approve")
public class ApproveController {

    private final ApproveService approveService;

    @GetMapping("/company/apply")
    public ResponseEntity<BaseResponseDto<List<CompanyApplyResponseDto>>> getCompanyApplyInfo() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(approveService.getCompanyApplyInfo()));
    }

    @PatchMapping("/company/apply/accept/{companyId}/{answer}")
    public ResponseEntity<BaseResponseDto<Long>> respondCompany(@PathVariable(name = "companyId") long companyId,
                                                               @PathVariable(name = "answer") String answer) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(approveService.respondCompany(companyId, answer)));
    }
}
