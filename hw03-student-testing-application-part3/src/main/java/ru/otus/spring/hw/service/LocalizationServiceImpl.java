package ru.otus.spring.hw.service;

import org.springframework.context.MessageSource;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.config.AppProps;

@RequiredArgsConstructor
public class LocalizationServiceImpl implements LocalizationService {
    private final AppProps props;
    private final MessageSource messageSource;

    @Override
    public String getText(String key, Object... args) {
        return messageSource.getMessage(key, args, props.getLocale());
    }
}
