package com.notes.controller;

import com.notes.records.SignupRequest;
import com.notes.service.NoteUserService;
import com.notes.service.RegistrationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@AllArgsConstructor
@Controller
@RequestMapping(path = "/signup")
public class RegistrationController {
	
	NoteUserService noteUserService;
	RegistrationService registrationService;
	
	@GetMapping
	public String signup(Model model) {
		model.addAttribute("signupRequest", new SignupRequest());
		return "signup";
	}
	
	@PostMapping(path = "/process")
	public String signup(HttpServletRequest request, RedirectAttributes attributes,
		@ModelAttribute("signupRequest") SignupRequest signupRequest) {
		
		String referer = request.getHeader("referer");
		if (referer != null) {
			attributes.addFlashAttribute("referer", referer);
		}
		
		registrationService.register(signupRequest);
		return "redirect:/login";
	}
	
	@GetMapping(path = "/confirm")
	public String confirmEmail(@RequestParam("token") String token,
							   RedirectAttributes redirectAttributes,
	                           Model model) {
		
		String fullName = registrationService.confirmToken(token);
		model.addAttribute("fullName", fullName);
		redirectAttributes.addFlashAttribute("confirmation","Your email is confirmed, try logging in");
		return "redirect:/login";
	}
	
}
