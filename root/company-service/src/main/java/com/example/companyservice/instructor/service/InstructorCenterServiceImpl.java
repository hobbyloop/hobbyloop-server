package com.example.companyservice.instructor.service;

import com.example.companyservice.common.exception.ApiException;
import com.example.companyservice.common.exception.ExceptionEnum;
import com.example.companyservice.company.entity.Center;
import com.example.companyservice.company.repository.CenterRepository;
import com.example.companyservice.instructor.dto.request.CreateInstructorCenterRequestDto;
import com.example.companyservice.instructor.dto.response.CreateInstructorCenterResponseDto;
import com.example.companyservice.instructor.dto.response.InstructorCenterResponseDto;
import com.example.companyservice.instructor.entity.Instructor;
import com.example.companyservice.instructor.entity.InstructorAuthEnum;
import com.example.companyservice.instructor.entity.InstructorCenter;
import com.example.companyservice.instructor.repository.instructorCenter.InstructorCenterRepository;
import com.example.companyservice.instructor.repository.InstructorRepository;
import com.example.companyservice.lecture.dto.response.LectureInfoResponseDto;
import com.example.companyservice.lecture.entity.Lecture;
import com.example.companyservice.lecture.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InstructorCenterServiceImpl implements InstructorCenterService {

    private final InstructorCenterRepository instructorCenterRepository;

    private final InstructorRepository instructorRepository;

    private final CenterRepository centerRepository;

    private final LectureRepository lectureRepository;

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

    @Override
    @Transactional(readOnly = true)
    public List<InstructorCenterResponseDto> getInstructorCenter(long centerId, int sort) {
        List<InstructorCenterResponseDto> result = new ArrayList<>();
        List<InstructorCenter> instructorCenterList = instructorCenterRepository.getInstructorCenterList(centerId, sort);
        instructorCenterList.forEach(ic -> {
            List<Lecture> lectureList = lectureRepository.findAllByInstructorCenterId(ic.getId());
            List<LectureInfoResponseDto> lectureDtoList = lectureList.stream().map(LectureInfoResponseDto::from).toList();
            result.add(InstructorCenterResponseDto.of(ic, lectureDtoList));
        });
        return result;
    }

    @Override
    @Transactional
    public void updateInstructorCenterAuth(long instructorCenterId, int auth) {
        InstructorAuthEnum.findByValue(auth);
        InstructorCenter instructorCenter = instructorCenterRepository.findById(instructorCenterId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.INSTRUCTORCENTER_NOT_EXIST_EXCEPTION));
        instructorCenter.updateAuth(auth);
    }
}
