package ru.otus.spring.hw.controllers.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import ru.otus.spring.hw.controllers.dto.GenreDto;
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

    @Test
    void shouldCreateGenreFromDto() {
        final var genreDto = new GenreDto();
        genreDto.setId("123");
        genreDto.setName("genre name");
        final var genre = genreDto.toEntity();
        assertThat(genre.getId()).isEqualTo(genreDto.getId());
        assertThat(genre.getName()).isEqualTo(genreDto.getName());
    }
}
