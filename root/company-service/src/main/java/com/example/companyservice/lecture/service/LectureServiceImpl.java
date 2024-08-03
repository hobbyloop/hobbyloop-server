package com.example.companyservice.lecture.service;

import com.example.companyservice.lecture.dto.request.CreateLectureRequestDto;
import com.example.companyservice.lecture.dto.response.CreateLectureResponseDto;
import com.example.companyservice.lecture.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;

    @Override
    @Transactional
    public CreateLectureResponseDto createLecture(long centerId, CreateLectureRequestDto requestDto) {

        return null;
    }
}
