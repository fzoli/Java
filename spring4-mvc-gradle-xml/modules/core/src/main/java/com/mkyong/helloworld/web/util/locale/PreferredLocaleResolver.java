package com.mkyong.helloworld.web.util.locale;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

public class PreferredLocaleResolver extends AcceptHeaderLocaleResolver {

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        Locale l = super.resolveLocale(request);
        if (StringUtils.isEmpty(l.getLanguage())) {
            return Locale.getDefault();
        }
        return new Locale(l.getLanguage());
    }

}
