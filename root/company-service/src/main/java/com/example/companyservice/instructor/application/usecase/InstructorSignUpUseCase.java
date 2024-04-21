package com.example.companyservice.instructor.application.usecase;

import com.example.companyservice.instructor.application.dto.InstructorSignUpCommand;
import com.example.companyservice.instructor.application.dto.InstructorSignUpResult;

public interface InstructorSignUpUseCase {
	InstructorSignUpResult signUp(InstructorSignUpCommand command);
}
