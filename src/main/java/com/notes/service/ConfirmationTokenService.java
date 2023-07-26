package com.notes.service;

import com.notes.entity.ConfirmationToken;
import com.notes.repository.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ConfirmationTokenService {
	
	private final ConfirmationTokenRepository confirmationTokenRepository;
	
	public void saveConfirmationToken(ConfirmationToken token) {
		confirmationTokenRepository.save(token);
	}
	
	public Optional<ConfirmationToken> getConfirmationToken(String token) {
		return confirmationTokenRepository.findByToken(token);
	}
	
	public void updateConfirmationTime(String token) {
		confirmationTokenRepository.updateConfirmedAt(token, LocalDateTime.now());
	}
	
	public void deleteByUsername(String username) {
		confirmationTokenRepository.deleteByNoteUser_Username(username);
	}
	
	public void deleteByEmail(String email) {
		confirmationTokenRepository.deleteByNoteUser_Email(email);
	}
	
	public Optional<String> findUsernameByToken(String token) {
		return confirmationTokenRepository.findNoteUserByToken(token);
	}
}
