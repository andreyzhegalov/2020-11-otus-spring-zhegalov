package ru.otus.spring.hw.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@SpringBootTest
class IOServiceConsoleTest {

    @Import(IOServiceConsole.class)
    @Configuration
    public static class IOServiceConsoleInner {
    }

    @MockBean
    private PrintStream printStream;

    @MockBean
    private InputStream inputStream;

    @Autowired
    private IOService ioService;

    @Test
    void shouldPrintToPrintStreamWhenPrintData() {
        final String data = "test";
        ioService.print(data);

        then(printStream).should().println(eq(data));
    }

    @Test
    void testReadData() {
        final String inputMessage = "some message";
        final InputStream stubInputStream = new ByteArrayInputStream(inputMessage.getBytes());
        final String newData = new IOServiceConsole(printStream, stubInputStream).read();
        assertThat(newData).isEqualTo(inputMessage);
    }
}
