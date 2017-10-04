package com.mkyong.helloworld.web;

import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

public class PreferredLocaleResolver extends AcceptHeaderLocaleResolver {

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        Locale l = super.resolveLocale(request);
        return new Locale(PreferredLocaleContext.getPreferredLocale(l).getLanguage());
    }

}
