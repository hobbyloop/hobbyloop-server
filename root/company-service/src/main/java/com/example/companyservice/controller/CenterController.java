package com.example.companyservice.controller;

import com.example.companyservice.common.util.Utils;
import com.example.companyservice.dto.BaseResponseDto;
import com.example.companyservice.dto.request.CenterCreateRequestDto;
import com.example.companyservice.service.CenterService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CenterController {

    private final CenterService centerService;

    @PostMapping("/centers")
    public ResponseEntity<BaseResponseDto<Long>> createCenter(HttpServletRequest request,
                                                              @RequestPart CenterCreateRequestDto requestDto,
                                                              @RequestPart MultipartFile logo,
                                                              @RequestPart List<MultipartFile> centerImageList) {
        long companyId = Utils.parseAuthorizedUserId(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.createCenter(companyId, requestDto, logo, centerImageList)));
    }
}
