package com.notes.email;

import com.notes.exceptions.NotesAppException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@AllArgsConstructor
public class EmailSenderService implements EmailSender {
	
	JavaMailSender javaMailSender;
	TemplateEngine templateEngine;
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailSenderService.class);
	
	
	@Async
	@Override
	public void sendEmail(String to,
	                        String email,
	                        String subject) {
		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
			messageHelper.setFrom("notesappbyusman@gmail.com");
			messageHelper.setSubject(subject);
			messageHelper.setText(email, true);
			messageHelper.setTo(to);
			javaMailSender.send(mimeMessage);
		} catch (MessagingException e) {
			LOGGER.error("Failed to send email", e);
			throw new NotesAppException("Failed to send email");
		}
	}
	
	public String buildEmailForEmailVerification(String fullName,
	                                             String email,
	                                             String link,
	                                             String template) {
		
		Context context = new Context();
		context.setVariable("fullName", fullName);
		context.setVariable("email", email);
		context.setVariable("link", link);
		
		return templateEngine.process(template, context);
	}
	
}
