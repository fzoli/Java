package com.mkyong.helloworld.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Component
public class DateTimeFormatterFactory {

	@Autowired
	private PreferredLocaleContext localeContext;

	public DateTimeFormatter createLocalizedMediumDateTimeFormatter() {
		return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM).withLocale(localeContext.getPreferredLocale());
	}

}
