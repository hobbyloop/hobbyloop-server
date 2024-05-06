package com.example.companyservice.instructor.application.signup;

import java.time.LocalDate;

import com.example.companyservice.common.vo.EmailAddress;
import com.example.companyservice.common.vo.PhoneNumber;
import com.example.companyservice.instructor.domain.Gender;
import com.example.companyservice.instructor.domain.OauthProvider;

public record InstructorSignUpCommand(String name,
									  LocalDate dateOfBirth,
									  PhoneNumber phoneNumber,
									  Gender gender,
									  String ci,
									  String di,
									  String subject,
									  EmailAddress emailAddress,
									  OauthProvider oauthProvider,
									  String token,
									  boolean consentToMarketingCommunications,
									  boolean consentToPersonalInformation) {

}
