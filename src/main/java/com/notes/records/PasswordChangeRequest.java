package com.notes.records;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class PasswordChangeRequest {
	String username;
	String password;
	
	public PasswordChangeRequest(String username) {
		this.username = username;
	}
}
