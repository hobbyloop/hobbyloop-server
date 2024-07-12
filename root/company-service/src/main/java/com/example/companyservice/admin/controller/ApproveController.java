package com.example.companyservice.admin.controller;

import com.example.companyservice.admin.dto.response.CompanyApplyResponseDto;
import com.example.companyservice.admin.service.ApproveService;
import com.example.companyservice.common.dto.BaseResponseDto;
import com.example.companyservice.common.dto.PageInfo;
import com.example.companyservice.common.dto.PageableResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@Tag(name = "관리자시스템")
public class ApproveController {

    private final ApproveService approveService;

    @GetMapping("/company/approve/list")
    @Operation(summary = "승인 요청 업체 목록 조회", description = "입점 요청을 조회한다. default=등록순, 추후 파라미터값으로 광고/이벤트, 리뷰 블라인드 등등 추가 예정")
    public ResponseEntity<PageableResponse<List<CompanyApplyResponseDto>>> getCompanyApplyInfo(Pageable pageable) {
        Page<CompanyApplyResponseDto> companyApplyDtos = approveService.getCompanyApplyInfo(pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(PageableResponse.from(companyApplyDtos.getContent(), PageInfo.from(companyApplyDtos)));
    }

    @PatchMapping("/company/approve/{companyId}/{answer}")
    @Operation(summary = "업체 승인/거절 상태 변경")
    public ResponseEntity<BaseResponseDto<Long>> respondCompany(@PathVariable(name = "companyId") long companyId,
                                                               @PathVariable(name = "answer") String answer) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(approveService.respondCompany(companyId, answer)));
    }

    @GetMapping("/company/approve/detail/{companyId}")
    @Operation(summary = "승인 요청 상세 정보 조회")
    public ResponseEntity<BaseResponseDto> findDetail(@PathVariable(name = "companyId") long companyId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(approveService.findDetail(companyId)));
    }
}
