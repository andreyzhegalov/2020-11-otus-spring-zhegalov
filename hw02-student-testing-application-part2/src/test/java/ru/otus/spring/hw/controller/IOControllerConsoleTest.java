package ru.otus.spring.hw.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;

import java.io.PrintStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IOControllerConsoleTest {

    @Mock
    private PrintStream printStream;

    @Test
    void shouldPrintToPrintStreamWhenPrintData() {
        final String DATA = "test";
        new IOControllerConsole(printStream).print(DATA);

        then(printStream).should().println(eq(DATA));
    }
}
