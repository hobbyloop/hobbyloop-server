package com.example.companyservice.instructor.application.signup;

import java.time.LocalDate;

import com.example.companyservice.instructor.api.v1.signup.request.InstructorSignUpRequestV1;
import com.example.companyservice.instructor.domain.Gender;
import com.example.companyservice.instructor.domain.OauthProvider;

public record InstructorSignUpCommand(String name,
									  LocalDate dateOfBirth,
									  String phoneNumber,
									  Gender gender,
									  String ci,
									  String di,
									  String subject,
									  String email,
									  OauthProvider oauthProvider,
									  String token,
									  boolean consentToMarketingCommunications,
									  boolean consentToPersonalInformation) {

	public InstructorSignUpCommand(InstructorSignUpRequestV1 request) {
		this(
			request.profile().name(),
			request.profile().dateOfBirth(),
			request.profile().phoneNumber(),
			request.profile().gender(),
			request.secret().ci(),
			request.secret().di(),
			request.oauth().subject(),
			request.oauth().email(),
			request.oauth().provider(),
			request.oauth().token(),
			request.marketing().consentToMarketingCommunications(),
			request.marketing().consentToPersonalInformation()
			);
	}
}
