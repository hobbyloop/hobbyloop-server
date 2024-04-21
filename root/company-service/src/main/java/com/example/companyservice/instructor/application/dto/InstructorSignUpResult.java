package com.example.companyservice.instructor.application.dto;

import com.example.companyservice.instructor.domain.Instructor;
import com.example.companyservice.instructor.domain.InstructorOauth;
import com.example.companyservice.instructor.domain.InstructorProfile;

public record InstructorSignUpResult(Instructor instructor,
									 InstructorProfile profile,
									 InstructorOauth oauth) {
}
