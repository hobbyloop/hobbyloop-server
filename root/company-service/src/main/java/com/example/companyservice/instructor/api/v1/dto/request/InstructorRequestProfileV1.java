package com.example.companyservice.instructor.api.v1.dto.request;

import java.time.LocalDate;

import com.example.companyservice.instructor.domain.Gender;

public record InstructorRequestProfileV1(String name,
										 LocalDate dateOfBirth,
										 String phoneNumber,
										 Gender gender) {
}
