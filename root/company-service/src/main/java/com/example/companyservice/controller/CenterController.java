package com.example.companyservice.controller;

import com.example.companyservice.common.util.Utils;
import com.example.companyservice.dto.BaseResponseDto;
import com.example.companyservice.dto.request.CenterUpdateRequestDto;
import com.example.companyservice.dto.response.CenterCompanyResponseDto;
import com.example.companyservice.dto.request.CenterCreateRequestDto;
import com.example.companyservice.dto.response.CenterCreateResponseDto;
import com.example.companyservice.dto.response.CenterHomeResponseDto;
import com.example.companyservice.dto.response.CenterResponseListDto;
import com.example.companyservice.service.CenterService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CenterController {

    private final CenterService centerService;

    @PostMapping("/centers")
    public ResponseEntity<BaseResponseDto<CenterCreateResponseDto>> createCenter(HttpServletRequest request,
                                                                                 @RequestBody CenterCreateRequestDto requestDto) {
        long companyId = Utils.parseAuthorizedUserId(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseDto<>(centerService.createCenter(companyId, requestDto)));
    }

    @GetMapping("/centers")
    public ResponseEntity<BaseResponseDto<CenterResponseListDto>> getCenterList(HttpServletRequest request) {
        long companyId = Utils.parseAuthorizedUserId(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.getCenterList(companyId)));
    }

    @GetMapping("/centers/admin-home/{centerId}")
    public ResponseEntity<BaseResponseDto<CenterHomeResponseDto>> getCenterHome(@PathVariable long centerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.getCenterHome(centerId)));
    }

    @GetMapping("/centers/admin-ticket/{centerId}")
    public ResponseEntity<BaseResponseDto<CenterCompanyResponseDto>> getCenterCompany(@PathVariable long centerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.getCenterCompany(centerId)));
    }

    @PatchMapping("/centers/{centerId}")
    public ResponseEntity<BaseResponseDto<Long>> updateCenter(@PathVariable long centerId,
                                                              @RequestBody CenterUpdateRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.updateCenter(centerId, requestDto)));
    }

    @PostMapping("/centers/quick-button/{centerId}")
    public ResponseEntity<BaseResponseDto<Void>> updateQuickButton(@PathVariable long centerId,
                                                                   @RequestBody List<Integer> requestDto) {
        centerService.updateQuickButton(centerId, requestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>());
    }
}
