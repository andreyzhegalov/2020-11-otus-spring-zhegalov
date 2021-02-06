package ru.otus.spring.hw.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import ru.otus.spring.hw.model.Genre;

public class GenreDtoTest {
    @Test
    void shouldCreateDtoFromEntity() {
        final var genre = new Genre();
        genre.setId("123");
        genre.setName("genre");
        final var genreDto = new GenreDto(genre);
        assertThat(genreDto.getId()).isEqualTo(genre.getId());
        assertThat(genreDto.getName()).isEqualTo(genre.getName());
    }
}
