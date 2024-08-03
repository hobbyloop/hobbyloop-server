package com.example.companyservice.instructor.service;

import com.example.companyservice.common.exception.ApiException;
import com.example.companyservice.common.exception.ExceptionEnum;
import com.example.companyservice.company.entity.Center;
import com.example.companyservice.company.repository.CenterRepository;
import com.example.companyservice.instructor.dto.request.CreateInstructorCenterRequestDto;
import com.example.companyservice.instructor.dto.response.CreateInstructorCenterResponseDto;
import com.example.companyservice.instructor.entity.Instructor;
import com.example.companyservice.instructor.entity.InstructorCenter;
import com.example.companyservice.instructor.repository.InstructorCenterRepository;
import com.example.companyservice.instructor.repository.InstructorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InstructorCenterServiceImpl implements InstructorCenterService {

    private final InstructorCenterRepository instructorCenterRepository;

    private final InstructorRepository instructorRepository;

    private final CenterRepository centerRepository;

    @Override
    @Transactional
    public CreateInstructorCenterResponseDto createInstructorCenter(long centerId, CreateInstructorCenterRequestDto requestDto) {
        Center center = centerRepository.findById(centerId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.CENTER_NOT_EXIST_EXCEPTION));
        Instructor instructor = instructorRepository.findByNameAndBirthdayAndPhoneNumberAndIsDeleteFalse(requestDto.getName(), requestDto.getBirthday(), requestDto.getPhoneNumber())
                .orElseThrow(() -> new ApiException(ExceptionEnum.INSTRUCTOR_NOT_EXIST_EXCEPTION));
        InstructorCenter instructorCenter = InstructorCenter.of(instructor, center);
        instructorCenterRepository.save(instructorCenter);
        return CreateInstructorCenterResponseDto.from(instructor);
    }
}
