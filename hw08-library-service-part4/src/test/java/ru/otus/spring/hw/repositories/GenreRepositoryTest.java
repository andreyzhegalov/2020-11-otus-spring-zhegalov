package ru.otus.spring.hw.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class GenreRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @Test
    void shouldReturnCorrectGenreList() {
        final var genres = genreRepository.findAll();
        assertThat(genres).hasSize(3);
    }
}
