package com.example.companyservice.instructor.api.v1.dto.request;

public record InstructorSignUpRequestV1(InstructorRequestProfileV1 profile,
										InstructorRequestSecretV1 secret,
										InstructorRequestOauthV1 oauth,
										InstructorRequestMarketingV1 marketing) {
}