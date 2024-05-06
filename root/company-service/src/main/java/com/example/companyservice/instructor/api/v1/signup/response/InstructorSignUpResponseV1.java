package com.example.companyservice.instructor.api.v1.signup.response;

import java.time.format.DateTimeFormatter;

import com.example.companyservice.instructor.application.signup.InstructorSignUpResult;
import com.example.companyservice.instructor.domain.Instructor;
import com.example.companyservice.instructor.domain.InstructorProfile;
import com.example.companyservice.instructor.domain.InstructorStatus;

public record InstructorSignUpResponseV1(Long id,
										 String email,
										 InstructorResponseProfileV1 profile,
										 InstructorStatus status,
										 InstructorResponseMarketingV1 marketing,
										 String signUpAt) {
	public static InstructorSignUpResponseV1 from(InstructorSignUpResult result) {
		Instructor instructor = result.instructor();
		InstructorProfile profile = result.profile();

		return new InstructorSignUpResponseV1(
			instructor.getId(),
			instructor.getEmailAddress(),
			new InstructorResponseProfileV1(
				profile.getName(),
				profile.getDateOfBirth(),
				profile.getPhoneNumber(),
				profile.getGender()
			),
			instructor.getStatus(),
			new InstructorResponseMarketingV1(
				instructor.isConsentToMarketingCommunications(),
				instructor.isConsentToPersonalInformation()
			),
			instructor.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
		);
	}
}
