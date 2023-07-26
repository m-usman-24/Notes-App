package com.notes.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
	                                    HttpServletResponse response,
	                                    AuthenticationException exception)
		throws IOException, ServletException {
		
		String exceptionName = exception.getClass().getSimpleName();
		StringBuilder failureUrl = new StringBuilder("/login?error=");
		
		for (int i = 0 ; i < exceptionName.length() ; i++) {
			char ch = exceptionName.charAt(i);
			if (Character.isUpperCase(ch)) {
				failureUrl.append(Character.toLowerCase(ch));
			}
		}
		
		getRedirectStrategy().sendRedirect(request, response, failureUrl.toString());
	}
}
