package ru.otus.spring.hw.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
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

import ru.otus.spring.hw.dto.AuthorDto;
import ru.otus.spring.hw.model.Author;

@SpringBootTest
public class IOAuthorServiceTest {
    @Import(IOAuthorService.class)
    @Configuration
    public static class IOAuthorServiceTestInner {
    }

    @Autowired
    private IOAuthorService ioAuthorService;

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
        given(ioService.read()).willReturn("1");

        assertThat(ioAuthorService.getAuthor()).isInstanceOf(AuthorDto.class);

        then(ioService).should(atLeastOnce()).print(anyString());
        then(ioService).should(atLeastOnce()).read();
    }

}
