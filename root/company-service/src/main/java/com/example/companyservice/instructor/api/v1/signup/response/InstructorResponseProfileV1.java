package com.example.companyservice.instructor.api.v1.signup.response;

import java.time.LocalDate;

import com.example.companyservice.instructor.domain.Gender;

public record InstructorResponseProfileV1(String name,
										  LocalDate dateOfBirth,
										  String phoneNumber,
										  Gender gender) {
}
