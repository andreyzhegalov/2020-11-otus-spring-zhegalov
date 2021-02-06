package ru.otus.spring.hw.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ru.otus.spring.hw.dto.GenreDto;
import ru.otus.spring.hw.model.Genre;
import ru.otus.spring.hw.repositories.GenreRepository;

@SpringBootTest
public class GenreServiceImplTest {

    @Import(GenreServiceImpl.class)
    @Configuration
    public static class TestContext {
    }

    @Autowired
    private GenreService genreService;

    @MockBean
    private GenreRepository genreRepository;

    @Captor
    private ArgumentCaptor<Genre> captor;

    @Test
    void shouldSaveGenreDto() {
        final var genreDto = new GenreDto();
        genreDto.setId("123");
        genreDto.setName("genre name");

        genreService.saveGenreDto(genreDto);
        then(genreRepository).should().save(captor.capture());

        assertThat(captor.getValue().getId()).isEqualTo(genreDto.getId());
        assertThat(captor.getValue().getName()).isEqualTo(genreDto.getName());
    }

    @Test
    void shouldReturnAllGenres() {
        genreService.findAllDto();
        then(genreRepository).should().findAll();
    }

    @Test
    void shouldDeleteById() {
        final var deletedId = "123";
        genreService.deleteById(deletedId);
        then(genreRepository).should().deleteById(eq(deletedId));
    }
}
