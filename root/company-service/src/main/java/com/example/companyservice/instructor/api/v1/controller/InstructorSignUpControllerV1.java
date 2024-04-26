package com.example.companyservice.instructor.api.v1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.companyservice.instructor.api.v1.dto.request.InstructorSignUpRequestV1;
import com.example.companyservice.instructor.api.v1.dto.response.InstructorSignUpResponseV1;
import com.example.companyservice.instructor.application.dto.InstructorSignUpCommand;
import com.example.companyservice.instructor.application.dto.InstructorSignUpResult;
import com.example.companyservice.instructor.application.usecase.InstructorSignUpUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/instructors")
@RequiredArgsConstructor
public class InstructorSignUpControllerV1 {
	private final InstructorSignUpUseCase instructorSignUpUseCase;

	@PostMapping("/sign-up")
	public ResponseEntity<InstructorSignUpResponseV1> signUp(@RequestBody final InstructorSignUpRequestV1 request) {
		InstructorSignUpResult signUpResult = instructorSignUpUseCase.signUp(new InstructorSignUpCommand(request));

		return ResponseEntity
			.status(201)
			.body(new InstructorSignUpResponseV1(signUpResult));
	}
}
