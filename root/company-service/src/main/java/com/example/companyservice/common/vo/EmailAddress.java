package com.example.companyservice.common.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EmailAddress {
	@Column(name = "email_address", nullable = false)
	private String value;

	public EmailAddress(String value) {
		EmailAddressValidator.validate(value);

		this.value = value;
	}

	public void changeTo(String newAddress) {
		EmailAddressValidator.validate(newAddress);

		this.value = newAddress;
	}
}
