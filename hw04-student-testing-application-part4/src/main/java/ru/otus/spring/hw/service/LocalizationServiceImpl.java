package ru.otus.spring.hw.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.config.AppProps;

@Service
@RequiredArgsConstructor
public class LocalizationServiceImpl implements LocalizationService {
    private final AppProps props;
    private final MessageSourceService messageSource;

    @Override
    public String getText(String key, Object... args) {
        return messageSource.getMessage(key, args, props.getLocale());
    }
}
