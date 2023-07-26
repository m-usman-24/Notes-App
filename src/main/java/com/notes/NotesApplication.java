package com.notes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class NotesApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(NotesApplication.class, args);
	}
}
