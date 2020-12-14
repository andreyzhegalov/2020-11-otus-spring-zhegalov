package ru.otus.spring.hw.service;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;

import ru.otus.spring.hw.config.AppProps;

@SpringBootTest
class LocalizationServiceImplTest {
    @Autowired
    private AppProps props;

    @MockBean
    private MessageSource messageSource;

    @Test
    void getText() {
        new LocalizationServiceImpl(props, messageSource).getText("key", "arg1", 1);

        then(messageSource).should().getMessage(eq("key"), eq(new Object[] { "arg1", 1 }), eq(props.getLocale()));
    }
}
