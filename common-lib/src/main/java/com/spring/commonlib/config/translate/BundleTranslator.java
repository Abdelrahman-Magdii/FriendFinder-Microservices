package com.spring.commonlib.config.translate;

import com.spring.commonlib.model.bundle.BundleErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Slf4j
@Component
public class BundleTranslator {

    private final MessageSource messageSource;

    @Autowired
    public BundleTranslator(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String code) {
        Locale locale = LocaleContextHolder.getLocale();
        log.info("Retrieving message for code: " + code);
        return messageSource.getMessage(code, null, locale);
    }

    public BundleErrorMessage getMessages(String code) {
        return new BundleErrorMessage(
                        messageSource.getMessage(code, null, new Locale("ar")),
                        messageSource.getMessage(code, null, new Locale("en")));
    }
}
