package com.example.companyservice.instructor.api.v1.signup.response;

import java.time.format.DateTimeFormatter;

import com.example.companyservice.instructor.application.signup.InstructorSignUpResult;
import com.example.companyservice.instructor.domain.InstructorStatus;

public record InstructorSignUpResponseV1(Long id,
										 String email,
										 InstructorResponseProfileV1 profile,
										 InstructorStatus status,
										 InstructorResponseMarketingV1 marketing,
										 String signUpAt) {
	public InstructorSignUpResponseV1(InstructorSignUpResult result) {
		this(
			result.instructor().getId(),
			result.instructor().getEmail(),
			new InstructorResponseProfileV1(
				result.profile().getName(), result.profile().getDateOfBirth(),
				result.profile().getPhoneNumber(), result.profile().getGender()),
			result.instructor().getStatus(),
			new InstructorResponseMarketingV1(
				result.instructor().isConsentToMarketingCommunications(),
				result.instructor().isConsentToMarketingCommunications()),
			result.instructor().getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
		);
	}
}
