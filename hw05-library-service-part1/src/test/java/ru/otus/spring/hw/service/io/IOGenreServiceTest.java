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

import ru.otus.spring.hw.model.Genre;

@SpringBootTest
public class IOGenreServiceTest {
    @Import(IOGenreService.class)
    @Configuration
    public static class IOGenreServiceTestInner {
    }

    @Autowired
    private IOModelService<Genre> ioGenreService;

    @MockBean
    private IOService ioService;

    @Test
    void printGenreShouldPrintViaIOService() {
        final var genre1 = new Genre(1L, "genre");
        final var genre2 = new Genre(2L, "genre");

        ioGenreService.print(List.of(genre1, genre2));
        then(ioService).should(atLeastOnce()).print(anyString());
        then(ioService).should(never()).read();
    }

    @Test
    void shouldReadGenreFromIOService() {
        assertThatCode(() -> ioGenreService.get()).isInstanceOf(UnsupportedOperationException.class);
    }
}
