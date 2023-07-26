package com.notes.service;

import com.notes.entity.ConfirmationToken;
import com.notes.entity.NoteUser;
import com.notes.exceptions.NotesAppException;
import com.notes.records.SignupRequest;
import com.notes.validators.EmailValidator;
import com.notes.validators.PasswordValidator;
import com.notes.validators.UsernameValidator;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class RegistrationService {

	private final EmailValidator emailValidator;
	private final UsernameValidator usernameValidator;
	private final PasswordValidator passwordValidator;
	private final NoteUserService noteUserService;
	private final ConfirmationTokenService confirmationTokenService;
	private final PasswordEncoder passwordEncoder;
	
	public void register(SignupRequest request) {
		
		boolean isEmailValid = emailValidator.test(request.getEmail());
		boolean isUsernameValid = usernameValidator.test(request.getUsername());
		boolean isPasswordValid = passwordValidator.test(request.getPassword());
		
		if (!isEmailValid){
			throw new NotesAppException("Invalid email, signup with your personal email");
		}
		
		if (!isUsernameValid) {
			throw new NotesAppException("Only underscores & periods are allowed as symbols, no more than 30 characters");
		}
		
		if (!isPasswordValid) {
			throw new NotesAppException("Password must contain at least 6 characters");
		}
		
		noteUserService.signUp(new NoteUser(
			request.getUsername().toLowerCase(),
			passwordEncoder.encode(request.getPassword()),
			request.getEmail(),
			request.getFullName()
		));
	}
	
	@Transactional
	public String confirmToken(String token) {
		
		ConfirmationToken confirmationToken = confirmationTokenService
			.getConfirmationToken(token)
			.orElseThrow(() -> new NotesAppException("Invalid email verification token, try signing up"));
		
		if (confirmationToken.getConfirmedAt() != null) {
			throw new NotesAppException("Email already verified, if its you try logging in");
		}
		
		if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
			throw new NotesAppException("Email verification token expired, try signing up again");
		}
		
		confirmationTokenService.updateConfirmationTime(token);
		
		noteUserService.enableNoteUser(confirmationToken.getNoteUser().getUsername());
		
		return confirmationToken.getNoteUser().getFullName();
	}
}
