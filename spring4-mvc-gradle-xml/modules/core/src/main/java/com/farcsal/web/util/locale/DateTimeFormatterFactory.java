package com.farcsal.web.util.locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

@Component
public class DateTimeFormatterFactory {

	public DateTimeFormatter createLocalizedMediumDateTimeFormatter() {
		Locale locale = LocaleContextHolder.getLocale();
		return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM).withLocale(locale);
	}

}
