package com.example.companyservice.instructor.application.signup;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.companyservice.instructor.domain.Instructor;
import com.example.companyservice.instructor.domain.InstructorOauth;
import com.example.companyservice.instructor.domain.InstructorProfile;
import com.example.companyservice.instructor.infrastructure.persistence.InstructorOauthRepository;
import com.example.companyservice.instructor.infrastructure.persistence.InstructorProfileRepository;
import com.example.companyservice.instructor.infrastructure.persistence.InstructorRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class InstructorSignUpService implements InstructorSignUpUseCase {
	private final InstructorRepository instructorRepository;
	private final InstructorProfileRepository instructorProfileRepository;
	private final InstructorOauthRepository instructorOauthRepository;

	@Override
	public InstructorSignUpResult signUp(InstructorSignUpCommand command) {
		Instructor instructor = instructorRepository.save(
			new Instructor(
				command.email(),
				command.ci(),
				command.di(),
				command.consentToMarketingCommunications(),
				command.consentToPersonalInformation()
			));

		InstructorProfile profile = instructorProfileRepository.save(
			new InstructorProfile(
				command.name(),
				command.dateOfBirth(),
				command.phoneNumber(),
				command.gender(),
				instructor
			));

		InstructorOauth oauth = instructorOauthRepository.save(
			new InstructorOauth(
				command.subject(),
				command.oauthProvider(),
				command.token(),
				instructor
			)
		);

		return new InstructorSignUpResult(instructor, profile, oauth);
	}
}
