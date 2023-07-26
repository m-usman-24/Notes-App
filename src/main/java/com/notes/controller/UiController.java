package com.notes.controller;

import com.notes.service.NoteUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@AllArgsConstructor
@RestController
public class UiController {
	
	NoteUserService noteUserService;
	
	
	@PostMapping("/")
	@ResponseBody
	public ResponseEntity<String> setPreferredTheme(@RequestParam String theme) {
		
		ResponseCookie themeCookie =
			ResponseCookie
				.from("theme", theme)
				.path("/")
				.httpOnly(false)
				.secure(true)
				.maxAge(Duration.ofDays(365))
				.build();
		
		return ResponseEntity
				.ok()
				.header(HttpHeaders.SET_COOKIE, themeCookie.toString())
				.build();
	}
}
