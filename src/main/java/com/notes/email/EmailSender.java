package com.notes.email;

public interface EmailSender {
	void sendEmail(String to, String email, String subject);
	String buildEmailForEmailVerification(String fullName,
	                                             String email,
	                                             String link,
	                                             String template);
}
