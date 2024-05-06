package com.example.companyservice.instructor.api.v1.signup;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.companyservice.instructor.api.v1.signup.request.InstructorSignUpRequestV1;
import com.example.companyservice.instructor.api.v1.signup.response.InstructorSignUpResponseV1;
import com.example.companyservice.instructor.application.signup.InstructorSignUpResult;
import com.example.companyservice.instructor.application.signup.InstructorSignUpUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/instructors")
@RequiredArgsConstructor
public class InstructorSignUpControllerV1 {
	private final InstructorSignUpUseCase instructorSignUpUseCase;

	@PostMapping("/sign-up")
	public ResponseEntity<InstructorSignUpResponseV1> signUp(@RequestBody final InstructorSignUpRequestV1 request) {
		InstructorSignUpResult signUpResult = instructorSignUpUseCase.signUp(request.toCommand());

		return ResponseEntity
			.status(201)
			.body(InstructorSignUpResponseV1.from(signUpResult));
	}
}
