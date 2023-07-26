package com.notes.service;

import com.notes.email.EmailSender;
import com.notes.email.EmailSenderService;
import com.notes.entity.ConfirmationToken;
import com.notes.entity.NoteUser;
import com.notes.entity.Roles;
import com.notes.exceptions.NotesAppException;
import com.notes.repository.NoteUserRepository;
import com.notes.validators.PasswordValidator;
import com.notes.validators.UsernameValidator;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@EqualsAndHashCode
@AllArgsConstructor
@Service
public class NoteUserService implements UserDetailsService {
	
	private static final String ERROR_MESSAGE = "User with %s not found!";
	private final NoteUserRepository noteUserRepository;
	private final PasswordEncoder passwordEncoder;
	private final ConfirmationTokenService confirmationTokenService;
	private final EmailSender emailSender;
	private final UsernameValidator usernameValidator;
	private final PasswordValidator passwordValidator;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return noteUserRepository.findNoteUserByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException
				(String.format(ERROR_MESSAGE, username)));
	}
	
	public Optional<NoteUser> findNoteUserByUsername(String username) {
		return noteUserRepository.findNoteUserByUsername(username);
	}
	
	@Transactional
	public void signUp(NoteUser noteUser) {
		
		boolean usernameTaken = noteUserRepository
			.findNoteUserByUsername(noteUser.getUsername())
			.isPresent();
		
		boolean emailTaken = noteUserRepository
			.findNoteUserByEmail(noteUser.getEmail())
			.isPresent();
		
		boolean isEnabled = noteUserRepository.
			findNoteUserWhereEnabled(noteUser.getUsername())
			.isPresent();
		
		if (emailTaken && isEnabled) {
			throw new NotesAppException("Email exists, if its you try logging in.");
		}
		
		if (emailTaken && !usernameTaken) {
			throw new NotesAppException("Account exists, enter your username and email to generate a new email " +
				"verification request");
		}
		if (usernameTaken) {
			throw new NotesAppException("Username taken!");
		}
		
		noteUserRepository.saveAndFlush(noteUser);
		
		String token = UUID.randomUUID().toString();
		
		ConfirmationToken confirmationToken = new ConfirmationToken(
			token,
			LocalDateTime.now(),
			LocalDateTime.now().plusMinutes(15),
			noteUser
		);
		
		confirmationTokenService.saveConfirmationToken(confirmationToken);
		
		String link = "http://www.localhost:8080/signup/confirm?token=" + token;
		
		String email = emailSender.buildEmailForEmailVerification(
			noteUser.getFullName(),
			noteUser.getEmail(),
			link,
			"emailVerification"
		);
		
		String subject = "VERIFY YOU EMAIL";
		
		emailSender.sendEmail(noteUser.getEmail(), email, subject);
	}
	
	public void enableNoteUser(String username) {
		
		NoteUser noteUser = noteUserRepository
			.findNoteUserByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException
				("Note user not found while enabling after email confirmation"));
		
		noteUser.setEnabled(true);
		noteUser.setLocked(false);
		noteUser.setRoles(Roles.USER);
	}
	
	@Transactional
	public void processRegeneratePasswordEmail(String userIdentifier) {
	
		boolean isUsername = usernameValidator.test(userIdentifier);
		boolean userExists = (isUsername)
							 ? noteUserRepository.existsById(userIdentifier)
							 : noteUserRepository.existsByEmailIgnoreCase(userIdentifier);
		NoteUser noteUser;
		
		if (!userExists) {
			throw new NotesAppException("User with this identifier do not exist");
		}
		
		if ((isUsername)) {
			noteUser = noteUserRepository
				.findNoteUserByUsername(userIdentifier)
				.orElseThrow(() -> new UsernameNotFoundException(
					String.format(ERROR_MESSAGE, userIdentifier)));
			confirmationTokenService.deleteByUsername(userIdentifier);
		} else {
			noteUser = noteUserRepository
				.findNoteUserByEmail(userIdentifier)
				.orElseThrow(() -> new UsernameNotFoundException(
					String.format(ERROR_MESSAGE, userIdentifier)));
			confirmationTokenService.deleteByEmail(userIdentifier);
		}
		
		String token = UUID.randomUUID().toString();
		
		ConfirmationToken confirmationToken = new ConfirmationToken(
			token,
			LocalDateTime.now(),
			LocalDateTime.now().plusMinutes(15),
			noteUser
		);
		
		confirmationTokenService.saveConfirmationToken(confirmationToken);
		
		String link = "http://www.localhost:8080/forgot/confirm?token=" + token;
		
		String email = emailSender.buildEmailForEmailVerification(
			noteUser.getFullName(),
			noteUser.getEmail(),
			link,
			"passwordChangeEmail"
		);
		
		String subject = "CHANGE YOUR PASSWORD";
		
		emailSender.sendEmail(noteUser.getEmail(), email, subject);
		
	}
	
	@Transactional
	public String validatePasswordChangeRequest(String token) {
		
		ConfirmationToken confirmationToken = confirmationTokenService
			.getConfirmationToken(token)
			.orElseThrow(() -> new NotesAppException("Invalid password change request, try again changing password"));
		
		if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
			throw new NotesAppException("Password change request is expired, try again changing password");
		}
		
		if (confirmationToken.getConfirmedAt() == null) {
			confirmationTokenService.updateConfirmationTime(token);
		}
		
		return confirmationTokenService
			.findUsernameByToken(token)
			.orElseThrow(() -> new UsernameNotFoundException(String.format(ERROR_MESSAGE, token)));
	}
	
	@Transactional
	public void changePassword(String username, String password, String token) {
		
		boolean isValidPassword = passwordValidator.test(password);
		
		if (!isValidPassword) {
			throw new NotesAppException(token + "|Ensure at least 6 characters of password length");
		}
		String encodedPassword = passwordEncoder.encode(password);
		
		NoteUser noteUser = noteUserRepository
			.findNoteUserByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException("User not found While updating password"));
		
		noteUser.setPassword(encodedPassword);
		
//		deleting the token after the user changes his password successfully
		confirmationTokenService.deleteByUsername(username);
	}
	
	@Transactional
	public void deleteUser(String credential) {
		
		String[] credentials = credential.split("/");
		
		String username = credentials[1];
		String password = credentials[2];
		
		String encodedPassword = noteUserRepository
			.findPasswordByUsername(username)
			.orElseThrow(() -> new NotesAppException("User not found with these credentials, logging you out for " +
				"security reasons"));
		
		System.out.println(encodedPassword);
		
		boolean isPasswordMatch = passwordEncoder.matches(password, encodedPassword);
		
		System.out.println(isPasswordMatch);
		
		if(isPasswordMatch) {
			noteUserRepository.deleteById(username);
		} else {
			throw  new NotesAppException("User not found with these credentials, logging you out for " +
				"security reasons");
		}
	}
}
