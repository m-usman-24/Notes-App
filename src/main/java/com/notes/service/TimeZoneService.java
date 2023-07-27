package com.notes.service;

import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class TimeZoneService {
	public ZonedDateTime clientDateTime() {
		return ZonedDateTime.now(ZoneId.of("UTC+5"));
	}
}
