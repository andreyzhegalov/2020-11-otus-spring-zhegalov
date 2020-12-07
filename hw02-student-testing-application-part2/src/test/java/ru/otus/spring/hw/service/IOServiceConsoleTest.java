package ru.otus.spring.hw.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;



@ExtendWith(MockitoExtension.class)
class IOServiceConsoleTest {

    @Mock
    private PrintStream printStream;

    @Mock
    private InputStream inputStream;

    @Test
    void shouldPrintToPrintStreamWhenPrintData() {
        final String data = "test";
        new IOServiceConsole(printStream, inputStream).print(data);

        then(printStream).should().println(eq(data));
    }

    @Test
    void testReadData() {
        final String inputMessage = "some message";
        final InputStream stubInputStream = new ByteArrayInputStream( inputMessage.getBytes() );
        final String newData = new IOServiceConsole(printStream, stubInputStream).read();
        assertThat(newData).isEqualTo(inputMessage);
    }
}
