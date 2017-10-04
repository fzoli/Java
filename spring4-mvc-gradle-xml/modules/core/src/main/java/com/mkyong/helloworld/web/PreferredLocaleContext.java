package com.mkyong.helloworld.web;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Locale;

@Component
public class PreferredLocaleContext {

	public Locale getPreferredLocale() {
		Locale locale = LocaleContextHolder.getLocale();
		return getPreferredLocale(locale);
	}

	public String getPreferredLanguageCode() {
		Locale locale = LocaleContextHolder.getLocale();
		return getPreferredLanguageCode(locale.getLanguage());
	}

	public static Locale getPreferredLocale(Locale locale) {
		String languageCode = getPreferredLanguageCode(locale.getLanguage());
		if (languageCode.equalsIgnoreCase(locale.getLanguage())) {
			return locale;
		}
		String countryCode = locale.getCountry();
		if (StringUtils.isEmpty(countryCode)) {
			return new Locale(languageCode);
		}
		return new Locale(languageCode, countryCode);
	}

	public static String getPreferredLanguageCode(String languageCode) {
		String[] knownLanguages = {"en", "hu"};
		for (String l : knownLanguages) {
			if (l.equalsIgnoreCase(languageCode)) {
				return l;
			}
		}
		return "en";
	}

}
