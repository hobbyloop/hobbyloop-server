package com.example.companyservice.lecture.service;

import com.example.companyservice.common.exception.ApiException;
import com.example.companyservice.common.exception.ExceptionEnum;
import com.example.companyservice.common.service.AmazonS3Service;
import com.example.companyservice.company.entity.Center;
import com.example.companyservice.company.repository.CenterRepository;
import com.example.companyservice.instructor.entity.InstructorCenter;
import com.example.companyservice.lecture.dto.request.CreateLectureRequestDto;
import com.example.companyservice.lecture.dto.response.CreateLectureResponseDto;
import com.example.companyservice.lecture.entity.Lecture;
import com.example.companyservice.lecture.entity.LectureBreakHour;
import com.example.companyservice.lecture.entity.LectureOperatingHour;
import com.example.companyservice.lecture.repository.LectureBreakHourRepository;
import com.example.companyservice.lecture.repository.LectureOperatingHourRepository;
import com.example.companyservice.lecture.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;
    private final LectureOperatingHourRepository lectureOperatingHourRepository;
    private final LectureBreakHourRepository lectureBreakHourRepository;
    private final AmazonS3Service amazonS3Service;
    private final CenterRepository centerRepository;

    @Override
    @Transactional
    public CreateLectureResponseDto createLecture(long centerId, CreateLectureRequestDto requestDto, MultipartFile lectureImage) {

        Center center = centerRepository.findById(centerId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.CENTER_NOT_EXIST_EXCEPTION));

        InstructorCenter instructorCenter = null;

        String lectureImageKey = "";
        String lectureImageUrl = "";
        if (lectureImage != null) {
            lectureImageKey = amazonS3Service.saveS3Img(lectureImage, "LectureImage");
            lectureImageUrl = amazonS3Service.getFileUrl(lectureImageKey);
        }

        Lecture lecture = requestDto.toEntity(center, instructorCenter, requestDto.getTicketId(), lectureImageKey, lectureImageUrl);
        List<LectureOperatingHour> lectureOperatingHours = requestDto.getLectureOperatingHours().stream()
                        .map(hourRequestDto -> {
                            return LectureOperatingHour.of(hourRequestDto, lecture);
                        })
                        .collect(Collectors.toList());
        List<LectureBreakHour> lectureBreakHours = requestDto.getLectureBreakHours().stream()
                        .map(hourRequestDto -> {
                            return LectureBreakHour.of(hourRequestDto, lecture);
                        })
                        .collect(Collectors.toList());


        lectureRepository.save(lecture);
        lectureOperatingHourRepository.saveAll(lectureOperatingHours);
        lectureBreakHourRepository.saveAll(lectureBreakHours);

        return CreateLectureResponseDto.of(lecture, requestDto.getTicketName(), center.getCenterName());
    }

}
