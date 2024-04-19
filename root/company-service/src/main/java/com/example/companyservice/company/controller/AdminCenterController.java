package com.example.companyservice.company.controller;

import com.example.companyservice.company.common.util.Utils;
import com.example.companyservice.company.dto.request.BusinessRequestDto;
import com.example.companyservice.company.dto.request.CenterUpdateRequestDto;
import com.example.companyservice.company.dto.response.*;
import com.example.companyservice.company.dto.BaseResponseDto;
import com.example.companyservice.company.dto.request.CenterCreateRequestDto;
import com.example.companyservice.company.service.CenterService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/centers")
public class AdminCenterController {

    private final CenterService centerService;

    @PostMapping()
    public ResponseEntity<BaseResponseDto<CenterCreateResponseDto>> createCenter(HttpServletRequest request,
                                                                                 @RequestPart(value = "requestDto") CenterCreateRequestDto requestDto,
                                                                                 @RequestPart(required = false, value = "logoImage") MultipartFile logoImage,
                                                                                 @RequestPart(required = false, value = "centerImageList") List<MultipartFile> centerImageList) {
        long companyId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseDto<>(centerService.createCenter(companyId, requestDto, logoImage, centerImageList)));
    }

    @GetMapping()
    public ResponseEntity<BaseResponseDto<CenterResponseListDto>> getCenterList(HttpServletRequest request) {
        long companyId = Utils.parseAuthorizedId(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.getCenterList(companyId)));
    }

    @GetMapping("/home/{centerId}")
    public ResponseEntity<BaseResponseDto<CenterHomeResponseDto>> getCenterHome(@PathVariable(value = "centerId") long centerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.getCenterHome(centerId)));
    }

    @GetMapping("/tickets/{centerId}")
    public ResponseEntity<BaseResponseDto<CenterBusinessResponseDto>> getCenterCompany(@PathVariable(value = "centerId") long centerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.getCenterBusiness(centerId)));
    }

    @PatchMapping("/{centerId}")
    public ResponseEntity<BaseResponseDto<Long>> updateCenter(@PathVariable(value = "centerId") long centerId,
                                                              @RequestPart(value = "requestDto") CenterUpdateRequestDto requestDto,
                                                              @RequestPart(required = false, value = "logoImage") MultipartFile logoImage,
                                                              @RequestPart(required = false, value = "centerImageList") List<MultipartFile> centerImageList) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.updateCenter(centerId, requestDto, logoImage, centerImageList)));
    }

    @PostMapping("/quick-button/{centerId}")
    public ResponseEntity<BaseResponseDto<Void>> updateQuickButton(@PathVariable(value = "centerId") long centerId,
                                                                   @RequestBody List<Integer> requestDto) {
        centerService.updateQuickButton(centerId, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponseDto<>());
    }

    @PatchMapping("/business/{centerId}")
    public ResponseEntity<BaseResponseDto<Long>> updateBusinessInfo(@PathVariable(value = "centerId") long centerId,
                                                                    @RequestBody BusinessRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.updateBusinessInfo(centerId, requestDto)));
    }

    @GetMapping("/original/{centerId}")
    public ResponseEntity<BaseResponseDto<OriginalCenterResponseDto>> getOriginalCenterInfo(@PathVariable(value = "centerId") long centerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.getOriginalCenterInfo(centerId)));
    }

    @GetMapping("/original/business/{centerId}")
    public ResponseEntity<BaseResponseDto<OriginalBusinessResponseDto>> getOriginalBusinessInfo(@PathVariable(value = "centerId") long centerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.getOriginalBusinessInfo(centerId)));
    }
}
