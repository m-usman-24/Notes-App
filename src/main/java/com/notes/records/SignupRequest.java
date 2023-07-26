package com.notes.records;

import lombok.*;

@ToString
@Data
public class SignupRequest {
	String fullName;
	String email;
	String username;
	String password;
}
