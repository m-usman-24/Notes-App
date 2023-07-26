package com.notes.service;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class TimeZoneService {
	@Bean
	public LocalDateTime clientDateTime() {
		LocalDateTime localDateTime = LocalDateTime.now();
		ZoneId zoneId = ZoneId.of("Asia/Karachi");
		ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
		return zonedDateTime.toLocalDateTime();
	}
}
