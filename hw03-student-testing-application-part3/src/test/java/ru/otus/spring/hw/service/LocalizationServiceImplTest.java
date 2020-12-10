package ru.otus.spring.hw.service;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.given;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import ru.otus.spring.hw.config.AppProps;

@ExtendWith(MockitoExtension.class)
class LocalizationServiceImplTest {

    @Mock
    private AppProps props;

    @Mock
    private MessageSource messageSource;

    @Test
    void getText() {
        final var locale = Locale.ENGLISH;
        given(props.getLocale()).willReturn(locale);

        new LocalizationServiceImpl(props, messageSource).getText("key", "arg1", "arg2");
        then(props).should().getLocale();
        then(messageSource).should().getMessage(eq("key"), eq(new Object[]{"arg1", "arg2"}), eq(locale));
    }
}
