package com.example.companyservice.controller;

import com.example.companyservice.common.util.Utils;
import com.example.companyservice.dto.BaseResponseDto;
import com.example.companyservice.dto.request.BusinessRequestDto;
import com.example.companyservice.dto.request.CenterUpdateRequestDto;
import com.example.companyservice.dto.response.*;
import com.example.companyservice.dto.request.CenterCreateRequestDto;
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
        long companyId = Utils.parseAuthorizedCompanyId(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseDto<>(centerService.createCenter(companyId, requestDto)));
    }

    @GetMapping("/centers")
    public ResponseEntity<BaseResponseDto<CenterResponseListDto>> getCenterList(HttpServletRequest request) {
        long companyId = Utils.parseAuthorizedCompanyId(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.getCenterList(companyId)));
    }

    @GetMapping("/centers/admin-home/{centerId}")
    public ResponseEntity<BaseResponseDto<CenterHomeResponseDto>> getCenterHome(@PathVariable long centerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.getCenterHome(centerId)));
    }

    @GetMapping("/centers/admin-ticket/{centerId}")
    public ResponseEntity<BaseResponseDto<CenterBusinessResponseDto>> getCenterCompany(@PathVariable long centerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.getCenterBusiness(centerId)));
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

    @PatchMapping("/centers/business-info/{centerId}")
    public ResponseEntity<BaseResponseDto<Long>> updateBusinessInfo(@PathVariable long centerId,
                                                                    @RequestBody BusinessRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.updateBusinessInfo(centerId, requestDto)));
    }

    @GetMapping("/centers/info/{centerId}")
    public ResponseEntity<BaseResponseDto<CenterInfoResponseDto>> getCenterInfo(@PathVariable long centerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.getCenterInfo(centerId)));
    }

    @GetMapping("/centers/info/detail/{centerId}")
    public ResponseEntity<BaseResponseDto<CenterInfoDetailResponseDto>> getCenterInfoDetail(HttpServletRequest request,
                                                                                            @PathVariable long centerId) {
        long memberId = Utils.parseAuthorizedMemberId(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.getCenterInfoDetail(centerId, memberId)));
    }
}
