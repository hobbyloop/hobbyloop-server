package com.example.companyservice.instructor.service;

import com.example.companyservice.instructor.dto.request.CreateInstructorCenterRequestDto;
import com.example.companyservice.instructor.dto.response.CreateInstructorCenterResponseDto;

public interface InstructorCenterService {
    CreateInstructorCenterResponseDto createInstructorCenter(long centerId, CreateInstructorCenterRequestDto requestDto);
}
