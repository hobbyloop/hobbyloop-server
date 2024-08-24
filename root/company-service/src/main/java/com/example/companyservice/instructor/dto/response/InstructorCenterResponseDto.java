package com.example.companyservice.instructor.dto.response;

import com.example.companyservice.instructor.entity.Instructor;
import com.example.companyservice.instructor.entity.InstructorCenter;
import com.example.companyservice.lecture.dto.response.LectureInfoResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstructorCenterResponseDto {

    private long id;

    private String name;

    private String phoneNumber;

    private List<LectureInfoResponseDto> lectureList;

    private int auth;

    public static InstructorCenterResponseDto of(InstructorCenter instructorCenter, List<LectureInfoResponseDto> lectureList) {
        return InstructorCenterResponseDto.builder()
                .id(instructorCenter.getId())
                .name(instructorCenter.getInstructor().getName())
                .phoneNumber(instructorCenter.getInstructor().getPhoneNumber())
                .lectureList(lectureList)
                .auth(instructorCenter.getAuth())
                .build();
    }
}
