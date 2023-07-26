package com.notes.config;

import com.notes.exceptions.NotesAppException;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(value = {NotesAppException.class})
	public String handleNotesAppExceptions(NotesAppException e,
	                                       RedirectAttributes attributes,
	                                       HttpSession session) {
		
		if (e.getMessage().contains("|")) {
			String[] tokenAndError = e.getMessage().split("\\|");
			
			tokenAndError[0] = tokenAndError[0].replace("|", "");
			
			attributes.addFlashAttribute("errorMessage", tokenAndError[1]);
			return "redirect:/forgot/confirm?token=" + tokenAndError[0];
			
		}
		
		String redirectTo = "redirect:/";
		attributes.addFlashAttribute("errorMessage", e.getMessage());
		
		switch (e.getMessage()) {
			
			case "Invalid password change request, try again changing password",
				 "Password change request is expired, try again changing password"
				 -> redirectTo += "login";
			
			case "User with this identifier do not exist" -> redirectTo += "forgot";
			
			case "User not found with these credentials, logging you out for security reasons" -> {
				session.invalidate();
				redirectTo += "login";
			}
			
			
			default -> redirectTo += "signup";
		}
		
		return redirectTo;
	}
	
	/*@ExceptionHandler(value = {Exception.class})
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleInternalServerError(Exception e, RedirectAttributes attributes) {
		attributes.addFlashAttribute("errorMessage", e);
		return "redirect:/signin";
	}*/
}
