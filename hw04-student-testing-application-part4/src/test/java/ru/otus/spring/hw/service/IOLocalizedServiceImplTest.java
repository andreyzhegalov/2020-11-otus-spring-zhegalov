package ru.otus.spring.hw.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@SpringBootTest
class IOLocalizedServiceImplTest {

    @Import(IOLocalizedServiceImpl.class)
    @Configuration
    public static class IOLocalizedServiceInner {
    }

    @MockBean
    private IOService ioService;

    @MockBean
    private LocalizationService localizationService;

    @Autowired
    private IOLocalizedService ioLocalizedService;

    @Test
    void shouldReadFromIOService() {
        ioLocalizedService.read();

        then(ioService).should().read();
    }

    @Test
    void shouldPrintTextFromLocalizationService() {
        given(localizationService.getText(any(), any())).willReturn("");

        ioLocalizedService.printLocalizedMessage("key", "arg1", "arg2");

        then(localizationService).should().getText(eq("key"), eq("arg1"), eq("arg2"));
        then(ioService).should().print(anyString());
    }
}
