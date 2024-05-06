package com.example.companyservice.common.vo;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PhoneNumberValidatorTest {

	@Test
	void givenNullThrowsIllegalArgumentException() {
		String value = null;

		assertThatIllegalArgumentException()
			.isThrownBy(() -> PhoneNumberValidator.validate(value));
	}

	@Test
	void givenBlankThrowsIllegalArgumentException() {
		String value = " ";

		assertThatIllegalArgumentException()
			.isThrownBy(() -> PhoneNumberValidator.validate(value));
	}

	@Test
	void givenNonPhoneNumberThrowsIllegalArgumentException() {
		String value = "NOT_PHONE_NUMBER";

		assertThatIllegalArgumentException()
			.isThrownBy(() -> PhoneNumberValidator.validate(value));
	}

	@Test
	void givenPhoneNumberWithHyphenThrowsNoException() {
		String value = "010-111-2222";

		assertThatNoException()
			.isThrownBy(() -> PhoneNumberValidator.validate(value));
	}

	@Test
	void givenPhoneNumberWithOutHyphenThrowsNoException() {
		String value = "0101112222";

		assertThatNoException()
			.isThrownBy(() -> PhoneNumberValidator.validate(value));
	}

}
