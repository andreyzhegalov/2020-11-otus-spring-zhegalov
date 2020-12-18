package ru.otus.spring.hw.service;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ru.otus.spring.hw.config.AppProps;

@SpringBootTest
class LocalizationServiceImplTest {

    @Import(LocalizationServiceImpl.class)
    @Configuration
    public static class LocalizationServiceInner {
    }

    @MockBean
    private AppProps props;

    @MockBean
    private MessageSourceService messageSource;

    @Autowired
    private LocalizationService localizationService;

    @Test
    void getText() {
        given(props.getLocale()).willReturn(Locale.ENGLISH);

        localizationService.getText("key", "arg1", 1);

        then(messageSource).should().getMessage(eq("key"), eq(new Object[] { "arg1", 1 }), eq(Locale.ENGLISH));
    }
}
