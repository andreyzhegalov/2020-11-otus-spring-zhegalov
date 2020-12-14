package ru.otus.spring.hw.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class IOLocalizedServiceImplTest {
    @MockBean
    private IOService ioService;

    @MockBean
    private LocalizationService localizationService;

    @Test
    void shouldReadFromIOService() {
        new IOLocalizedServiceImpl(ioService, localizationService).read();

        then(ioService).should().read();
    }

    @Test
    void shouldPrintTextFromLocalizationService() {
        given(localizationService.getText(any(), any())).willReturn("");

        new IOLocalizedServiceImpl(ioService, localizationService).printLocalizedMessage("key", "arg1", "arg2");

        then(localizationService).should().getText(eq("key"), eq("arg1"), eq("arg2"));
        then(ioService).should().print(anyString());
    }
}
