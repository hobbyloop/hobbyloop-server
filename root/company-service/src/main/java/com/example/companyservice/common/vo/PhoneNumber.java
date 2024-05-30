package com.example.companyservice.common.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PhoneNumber {
	@Column(name = "phone_number", nullable = false)
	private String value;

	public PhoneNumber(String value) {
		PhoneNumberValidator.validate(value);

		this.value = value;
	}

	public void changeTo(String newNumber) {
		PhoneNumberValidator.validate(newNumber);

		this.value = newNumber;
	}
}
