package com.example.companyservice.controller;

import com.example.companyservice.dto.BaseResponseDto;
import com.example.companyservice.dto.response.CenterInfoResponseDto;
import com.example.companyservice.service.CenterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/centers")
public class CenterClientController {
    private final CenterService centerService;

    @GetMapping("/info/{centerId}")
    public ResponseEntity<BaseResponseDto<CenterInfoResponseDto>> getCenterInfo(@PathVariable(value = "centerId") long centerId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseDto<>(centerService.getCenterInfo(centerId)));
    }
}
