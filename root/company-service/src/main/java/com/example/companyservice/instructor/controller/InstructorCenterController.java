package com.example.companyservice.instructor.controller;

import com.example.companyservice.common.dto.BaseResponseDto;
import com.example.companyservice.instructor.dto.request.CreateInstructorCenterRequestDto;
import com.example.companyservice.instructor.dto.response.CreateInstructorCenterResponseDto;
import com.example.companyservice.instructor.service.InstructorCenterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class InstructorCenterController {

    private final InstructorCenterService instructorCenterService;

    @PostMapping("/instructor-center/{centerId}")
    public ResponseEntity<BaseResponseDto<CreateInstructorCenterResponseDto>> createInstructorCenter(@PathVariable(name = "centerId") long centerId,
                                                                                                     @RequestBody CreateInstructorCenterRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseDto<>(instructorCenterService.createInstructorCenter(centerId, requestDto)));
    }
}
