package com.example.companyservice.common.vo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailAddressValidator {
	private static final Pattern emailAddressPattern;

	static {
		String emailAddressRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
		emailAddressPattern = Pattern.compile(emailAddressRegex);
	}

	public static void validate(String value) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException();
		}

		Matcher matcher = emailAddressPattern.matcher(value);

		if (!matcher.matches()) {
			throw new IllegalArgumentException();
		}
	}
}
