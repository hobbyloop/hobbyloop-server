package com.example.companyservice.lecture.service;

import com.example.companyservice.lecture.dto.request.CreateLectureRequestDto;
import com.example.companyservice.lecture.dto.response.CreateLectureResponseDto;

public interface LectureService {
    CreateLectureResponseDto createLecture(long centerId, CreateLectureRequestDto requestDto);
}
