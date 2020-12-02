package ru.otus.spring.hw.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IOControllerConsoleTest {

    @Mock
    private PrintStream printStream;

    @Mock
    private InputStream inputStream;

    @Test
    void shouldPrintToPrintStreamWhenPrintData() {
        final String DATA = "test";
        new IOControllerConsole(printStream, inputStream).print(DATA);

        then(printStream).should().println(eq(DATA));
    }

    @Test
    void testReadData() throws IOException{
        final var newData = new IOControllerConsole(printStream, inputStream).read();
        assertThat(newData).isEmpty();
    }
}
