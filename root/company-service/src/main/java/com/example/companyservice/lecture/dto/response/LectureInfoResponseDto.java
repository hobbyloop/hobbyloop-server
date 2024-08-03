package com.example.companyservice.lecture.dto.response;

import com.example.companyservice.lecture.entity.Lecture;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LectureInfoResponseDto {

    private Long lectureId;

    private String name;

    public static LectureInfoResponseDto from(Lecture lecture) {
        return LectureInfoResponseDto.builder()
                .lectureId(lecture.getId())
                .name(lecture.getName())
                .build();
    }
}
