package ru.otus.spring.hw.service.io;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.service.io.IOAuthorService;
import ru.otus.spring.hw.service.io.IOModelService;
import ru.otus.spring.hw.service.io.IOService;

@SpringBootTest
public class IOAuthorServiceTest {
    @Import(IOAuthorService.class)
    @Configuration
    public static class IOAuthorServiceTestInner {
    }

    @Autowired
    private IOModelService<Author> ioAuthorService;

    @MockBean
    private IOService ioService;

    @Test
    void printAuthorShouldPrintViaIOService() {
        final var author1 = new Author(1L, "author");
        final var author2 = new Author(2L, "author");

        ioAuthorService.print(List.of(author1, author2));
        then(ioService).should(atLeastOnce()).print(anyString());
        then(ioService).should(never()).read();
    }

    @Test
    void shouldReadAuthorFromIOService() {
        assertThatCode(() -> ioAuthorService.get()).isInstanceOf(UnsupportedOperationException.class);
    }
}
