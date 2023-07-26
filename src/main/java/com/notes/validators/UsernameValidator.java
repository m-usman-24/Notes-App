package com.notes.validators;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Service
public class UsernameValidator implements Predicate<String> {
	@Override
	public boolean test(String s) {
		return s.matches("^(?!.*[._-]{2})[a-zA-Z0-9._-]{1,30}$");
	}
}
