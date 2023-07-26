package com.notes.controller;

import com.notes.records.PasswordChangeRequest;
import com.notes.service.NoteUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@AllArgsConstructor
@RequestMapping
public class AuthenticationController {
	
	NoteUserService noteUserService;
	
	@GetMapping(path = "/login")
	public String login() {
		return "signin";
	}
	
	@PostMapping("/logout")
	public RedirectView logout() {
		return new RedirectView("/login?logout");
	}
	
	@GetMapping(path = "/forgot")
	public String forgot() {
		return "forgot";
	}
	
	@GetMapping(path = "/forgot/find")
	public String forgotRequest(@RequestParam("userIdentifier") String userIdentifier,
	                            RedirectAttributes redirectAttributes,
	                            HttpServletRequest request) {
		
		noteUserService.processRegeneratePasswordEmail(userIdentifier.toLowerCase());
		redirectAttributes.addFlashAttribute("referer", request.getHeader("referer"));
		
		return "redirect:/login";
	}
	
	
	@GetMapping(path = "/forgot/confirm")
	public String processForgot(@RequestParam("token") String token,
	                            Model model) {
		
		String username = noteUserService.validatePasswordChangeRequest(token);
		model.addAttribute("passwordChangeRequest", new PasswordChangeRequest(username));
		model.addAttribute("token", token);
		
		return "passwordChange";
	}
	
	@PostMapping(path = "/forgot/regenerate")
	public String changePassword(HttpServletRequest request,
        RedirectAttributes redirectAttributes,
		@ModelAttribute("passwordChangeRequest") PasswordChangeRequest passwordChangeRequest,
        @RequestParam("token") String token) {
		
		System.out.println("Hello" + token);
		
		noteUserService.changePassword(passwordChangeRequest.getUsername(),
									   passwordChangeRequest.getPassword(),
									   token);
		
		String referer = request.getHeader("referer");
		redirectAttributes.addFlashAttribute("referer", referer);
		return "redirect:/login";
	}
	
	@PostMapping(path = "/delete-user")
	public String deleteUser(@RequestParam("s") String validator,
	                         HttpSession session,
	                         RedirectAttributes attributes) {
		
		noteUserService.deleteUser(validator);
		session.invalidate();
		System.out.println("hello");
		attributes.addFlashAttribute("confirmation", "Your account has been deleted, bye bye!");
		return "redirect:/login";
	}
}
