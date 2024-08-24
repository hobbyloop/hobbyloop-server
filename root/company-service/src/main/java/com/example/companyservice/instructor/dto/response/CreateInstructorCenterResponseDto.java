package com.example.companyservice.instructor.dto.response;

import com.example.companyservice.instructor.entity.Instructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateInstructorCenterResponseDto {

    private String name;

    private String phoneNumber;

    private LocalDate birthday;

    public static CreateInstructorCenterResponseDto from(Instructor instructor) {
        return CreateInstructorCenterResponseDto.builder()
                .name(instructor.getName())
                .phoneNumber(instructor.getPhoneNumber())
                .birthday(instructor.getBirthday())
                .build();
    }
}
