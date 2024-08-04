package com.example.companyservice.lecture.service;

import com.example.companyservice.lecture.dto.request.CreateLectureRequestDto;
import com.example.companyservice.lecture.dto.response.CreateLectureResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface LectureService {
    CreateLectureResponseDto createLecture(long centerId, CreateLectureRequestDto requestDto, MultipartFile lectureImage);
}
