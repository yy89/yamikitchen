package com.xiaobudian.yamikitchen.common;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Locale;

/**
 * Created by Johnson on 2015/4/22.
 */
@Component(value = "localizedMessageSource")
public class LocalizedMessageSource {
    @Inject
    private MessageSource messageSource;

    public String getMessage(final String code) {
        return messageSource.getMessage(code, null, Locale.getDefault());
    }

    public String getMessage(final String code, Object[] params) {
        return messageSource.getMessage(code, params, Locale.getDefault());
    }

    public String getMessage(final String code, Object[] params, Locale locale) {
        return messageSource.getMessage(code, params, locale);
    }
}
