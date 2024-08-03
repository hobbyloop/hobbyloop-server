package com.example.companyservice.instructor.controller;

import com.example.companyservice.common.dto.BaseResponseDto;
import com.example.companyservice.common.dto.TokenResponseDto;
import com.example.companyservice.instructor.dto.request.CreateInstructorRequestDto;
import com.example.companyservice.instructor.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class InstructorController {

    private final InstructorService instructorService;

    @PostMapping("/join/instructor")
    public ResponseEntity<BaseResponseDto<TokenResponseDto>> createInstructor(@RequestBody CreateInstructorRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseDto<>(instructorService.createInstructor(requestDto)));
    }
}
