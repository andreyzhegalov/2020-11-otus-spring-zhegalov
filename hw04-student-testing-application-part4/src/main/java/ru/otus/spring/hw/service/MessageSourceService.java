package ru.otus.spring.hw.service;

import java.util.Locale;

import org.springframework.context.NoSuchMessageException;
import org.springframework.lang.Nullable;

public interface MessageSourceService {

    String getMessage(String code, @Nullable Object[] args, Locale locale) throws NoSuchMessageException;
}
