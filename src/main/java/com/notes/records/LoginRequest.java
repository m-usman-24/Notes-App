package com.notes.records;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@ToString
public class LoginRequest {
	private String username;
	private String password;
}
