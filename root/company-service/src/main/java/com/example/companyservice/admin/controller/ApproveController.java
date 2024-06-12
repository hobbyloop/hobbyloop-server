package com.example.companyservice.admin.controller;

import com.example.companyservice.admin.dto.response.CompanyApplyResponseDto;
import com.example.companyservice.admin.service.ApproveService;
import com.example.companyservice.common.dto.BaseResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/approve")
@Tag(name = "어드민 승인요청", description = "어드민 내 승인 페이지")
public class ApproveController {

    private final ApproveService approveService;

    @GetMapping("/company/apply")
    @Operation(summary = "입점 요청 승인 시설 조회", description = "최신 등록순으로 시설 조회")
    public ResponseEntity<BaseResponseDto<List<CompanyApplyResponseDto>>> getCompanyApplyInfo() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(approveService.getCompanyApplyInfo()));
    }

    @PatchMapping("/company/apply/accept/{companyId}/{answer}")
    @Operation(summary = "입점 요청 시설 승인 갱신")
    public ResponseEntity<BaseResponseDto<Long>> respondCompany(@Parameter(description = "시설 ID") @PathVariable(name = "companyId") long companyId,
                                                                @Parameter(description = "상태") @PathVariable(name = "answer") String answer) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(approveService.respondCompany(companyId, answer)));
    }
}


