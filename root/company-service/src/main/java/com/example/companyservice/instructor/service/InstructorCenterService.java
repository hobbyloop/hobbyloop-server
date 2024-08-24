package com.example.companyservice.instructor.service;

import com.example.companyservice.instructor.dto.request.CreateInstructorCenterRequestDto;
import com.example.companyservice.instructor.dto.response.CreateInstructorCenterResponseDto;
import com.example.companyservice.instructor.dto.response.InstructorCenterResponseDto;

import java.util.List;

public interface InstructorCenterService {
    CreateInstructorCenterResponseDto createInstructorCenter(long centerId, CreateInstructorCenterRequestDto requestDto);

    List<InstructorCenterResponseDto> getInstructorCenter(long centerId, int sort);

    void updateInstructorCenterAuth(long instructorCenterId, int auth);
}
