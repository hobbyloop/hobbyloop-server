package com.example.companyservice.instructor.api.v1.signup.request;

import com.example.companyservice.instructor.domain.OauthProvider;

public record InstructorRequestOauthV1(String subject,
									   String email,
									   OauthProvider provider,
									   String token) {
}
