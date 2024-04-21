package com.example.companyservice.instructor.api.v1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.companyservice.instructor.api.v1.dto.request.InstructorSignUpRequestV1;

@RestController
@RequestMapping("/api/v1/instructors")
public class InstructorSignUpControllerV1 {

	@PostMapping("/sign-up")
	public ResponseEntity<InstructorSignUpRequestV1> signUp(@RequestBody final InstructorSignUpRequestV1 request) {
		return ResponseEntity.ok(request);
	}
}
