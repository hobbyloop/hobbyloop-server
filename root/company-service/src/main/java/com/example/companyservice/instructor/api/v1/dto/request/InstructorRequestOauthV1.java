package com.example.companyservice.instructor.api.v1.dto.request;

public record InstructorRequestOauthV1(String subject,
									   String email,
									   String provider,
									   String token) {
}
