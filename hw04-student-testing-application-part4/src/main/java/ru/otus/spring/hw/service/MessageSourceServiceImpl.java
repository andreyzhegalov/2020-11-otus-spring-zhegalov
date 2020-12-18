package ru.otus.spring.hw.service;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MessageSourceServiceImpl implements MessageSourceService {
    private final MessageSource messageSource;

    @Override
    public String getMessage(String code, Object[] args, Locale locale) {
        try {
            return messageSource.getMessage(code, args, locale);
        } catch (NoSuchMessageException e) {
            throw new MessageSourceException(e);
        }
    }
}
