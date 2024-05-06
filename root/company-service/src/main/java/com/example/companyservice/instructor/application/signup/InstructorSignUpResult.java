package com.example.companyservice.instructor.application.signup;

import com.example.companyservice.instructor.domain.Instructor;
import com.example.companyservice.instructor.domain.InstructorProfile;

public record InstructorSignUpResult(Instructor instructor,
									 InstructorProfile profile) {
}
