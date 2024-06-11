package com.example.companyservice.admin.controller;

import com.example.companyservice.admin.dto.response.CompanyApplyResponseDto;
import com.example.companyservice.admin.service.ApproveService;
import com.example.companyservice.common.dto.BaseResponseDto;
import com.example.companyservice.common.dto.PageInfo;
import com.example.companyservice.common.dto.PageableResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<BaseResponseDto<List<CompanyApplyResponseDto>>> getCompanyApplyInfo(Pageable pageable) {
        Page<CompanyApplyResponseDto> companyApplyDtos = approveService.getCompanyApplyInfo(pageable);

        PageInfo pageInfo = PageInfo.from(
                pageable.getPageNumber(),
                companyApplyDtos.getTotalPages(),
                pageable.getPageSize(),
                companyApplyDtos.getTotalElements()
        );

        return ResponseEntity.status(HttpStatus.OK)
                .body(PageableResponse.from(companyApplyDtos.getContent(), pageInfo));
    }

    @PatchMapping("/company/apply/accept/{companyId}/{answer}")
    public ResponseEntity<BaseResponseDto<Long>> respondCompany(@PathVariable(name = "companyId") long companyId,
                                                               @PathVariable(name = "answer") String answer) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(approveService.respondCompany(companyId, answer)));
    }
}
