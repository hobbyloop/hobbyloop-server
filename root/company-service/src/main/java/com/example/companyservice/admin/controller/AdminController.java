package com.example.companyservice.admin.controller;

import com.example.companyservice.admin.service.AdminService;
import com.example.companyservice.auth.dto.request.AdminLoginRequestDto;
import com.example.companyservice.common.dto.BaseResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/join/admins")
    public ResponseEntity<BaseResponseDto<Long>> join(@RequestBody @Valid AdminLoginRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseDto<>(adminService.join(requestDto)));
    }
}
