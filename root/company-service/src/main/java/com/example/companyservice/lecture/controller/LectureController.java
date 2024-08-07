package com.example.companyservice.lecture.controller;

import com.example.companyservice.common.dto.BaseResponseDto;
import com.example.companyservice.lecture.dto.request.CreateLectureRequestDto;
import com.example.companyservice.lecture.dto.response.CreateLectureResponseDto;
import com.example.companyservice.lecture.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class LectureController {

    private final LectureService lectureService;

    @PostMapping("/lecture/{centerId}")
    public ResponseEntity<BaseResponseDto<CreateLectureResponseDto>> createLecture(@PathVariable(name = "centerId") long centerId,
                                                                                   @RequestBody CreateLectureRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseDto<>(lectureService.createLecture(centerId, requestDto)));
    }
}
