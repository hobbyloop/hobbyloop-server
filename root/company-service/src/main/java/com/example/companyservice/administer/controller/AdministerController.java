package com.example.companyservice.administer.controller;

import com.example.companyservice.administer.dto.request.AdministerRequestDto;
import com.example.companyservice.administer.dto.response.LoginResponseDto;
import com.example.companyservice.administer.service.AdministerService;
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
@RequestMapping("/api/v1/administer")
public class AdministerController {

    private final AdministerService administerService;

    @PostMapping("/join")
    public ResponseEntity<BaseResponseDto<Long>> join(@RequestBody @Valid AdministerRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseDto<>(administerService.join(requestDto)));
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponseDto<LoginResponseDto>> login(@RequestBody @Valid AdministerRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(administerService.login(requestDto)));
    }
}
