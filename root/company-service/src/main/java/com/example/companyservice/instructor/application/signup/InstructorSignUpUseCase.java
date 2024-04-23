package com.example.companyservice.instructor.application.signup;

/**
 * 개인정보, 본인인증값, 소셜로그인값, 마케팅동의 여부를 받아 회원가입을 진행한다.
 */
public interface InstructorSignUpUseCase {
	/**
	 *
	 * @param command 사용자로부터 입력받은 개인정보, 본인인증값, 소셜로그인값, 마케팅동의 여부를 담은 dto
	 * @return 생성된 instructor, instructor profile, instructor oauth 엔티티를 담은 dto
	 */
	InstructorSignUpResult signUp(InstructorSignUpCommand command);
}
