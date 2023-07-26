package com.notes.config;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@AllArgsConstructor
@EqualsAndHashCode
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final UserDetailsService userDetailsService;
	private final CustomAuthenticationFailureHandler failureHandler;
	private final PasswordEncoder passwordEncoder;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http
			.authorizeHttpRequests(configurer ->
				configurer
					.requestMatchers("/css/**")
					.permitAll()
					.requestMatchers("/javascript/**")
					.permitAll()
					.requestMatchers("/login/**")
					.permitAll()
					.requestMatchers("/signup/**")
					.permitAll()
					.requestMatchers("/forgot/**")
					.permitAll()
					.anyRequest()
					.authenticated()
			)
			.formLogin(configurer ->
				configurer
					.loginPage("/login")
					.loginProcessingUrl("/login")
					.failureHandler(failureHandler)
					.defaultSuccessUrl("/notes", true)
			)
			.authenticationProvider(daoAuthenticationProvider());
			
		return http.build();
	}
	
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder);
		
		return provider;
	}
	
}
