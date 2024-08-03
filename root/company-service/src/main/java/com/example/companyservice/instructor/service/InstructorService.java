package com.example.companyservice.instructor.service;

import com.example.companyservice.common.dto.TokenResponseDto;
import com.example.companyservice.instructor.dto.request.CreateInstructorRequestDto;

public interface InstructorService {
    TokenResponseDto createInstructor(CreateInstructorRequestDto requestDto);
}
