package com.example.companyservice.instructor.api.v1.signup.request;

import com.example.companyservice.common.vo.EmailAddress;
import com.example.companyservice.common.vo.PhoneNumber;
import com.example.companyservice.instructor.application.signup.InstructorSignUpCommand;

public record InstructorSignUpRequestV1(InstructorRequestProfileV1 profile,
										InstructorRequestSecretV1 secret,
										InstructorRequestOauthV1 oauth,
										InstructorRequestMarketingV1 marketing) {

	public InstructorSignUpCommand toCommand() {
		return new InstructorSignUpCommand(
			profile.name(),
			profile.dateOfBirth(),
			new PhoneNumber(profile().phoneNumber()),
			profile.gender(),
			secret.ci(),
			secret.di(),
			oauth.subject(),
			new EmailAddress(oauth.email()),
			oauth.provider(),
			oauth.oauth2AccessToken(),
			marketing.consentToMarketingCommunications(),
			marketing.consentToPersonalInformation()
		);
	}
}
