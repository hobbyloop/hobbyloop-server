package com.example.companyservice.common.vo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberValidator {
	private static final Pattern phoneNumberPattern;

	static {
		String phoneNumberRegex = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$";
		phoneNumberPattern = Pattern.compile(phoneNumberRegex);
	}


	public static void validate(String value) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException();
		}

		Matcher matcher = phoneNumberPattern.matcher(value);

		if (!matcher.matches()) {
			throw new IllegalArgumentException();
		}
	}
}
