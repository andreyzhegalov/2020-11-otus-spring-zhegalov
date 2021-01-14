package ru.otus.spring.hw.service;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import java.io.PrintStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ru.otus.spring.hw.io.ScannerInputStream;

@SpringBootTest
class IOServiceConsoleTest {

    @Import(IOServiceConsole.class)
    @Configuration
    public static class IOServiceConsoleInner {
    }

    @MockBean
    private PrintStream printStream;

    @MockBean
    private ScannerInputStream scannerInputStream;

    @Autowired
    private IOService ioService;

    @Test
    void shouldPrintToPrintStreamWhenPrintData() {
        final String data = "test";
        ioService.print(data);

        then(printStream).should().println(eq(data));
    }

    @Test
    void ioServiceShouldReadFromScanner() {
        given(scannerInputStream.hasNext()).willReturn(true);
        given(scannerInputStream.nextLine()).willReturn("any string");

        ioService.read();

        then(scannerInputStream).should().nextLine();
    }

    @Test
    void ioServiceShouldNotReadIfStreamIsEmpty() {
        given(scannerInputStream.hasNext()).willReturn(false);

        ioService.read();

        then(scannerInputStream).should(never()).nextLine();
    }
}
