package ru.otus.spring.hw.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ru.otus.spring.hw.controllers.dto.GenreDto;
import ru.otus.spring.hw.model.Genre;
import ru.otus.spring.hw.repositories.GenreRepository;

@SpringBootTest
public class GenreServiceImplTest {
    @Import(GenreServiceImpl.class)
    @Configuration
    public static class TestContext {
    }

    @Autowired
    private GenreService authorService;

    @MockBean
    private GenreRepository authorRepository;

    @Test
    void shouldReturnAllGenres() {
        final var expectedGenreList = List.of(new Genre());
        given(authorRepository.findAll()).willReturn(expectedGenreList);

        final var authorList = authorService.findAll();

        then(authorRepository).should().findAll();
        assertThat(authorList).hasSameElementsAs(expectedGenreList);
    }

    @Test
    void shouldSaveGenre() {
        final var savedGenre = new Genre();
        savedGenre.setId("123");
        given(authorRepository.save(any())).willReturn(savedGenre);
        authorService.saveGenre(new GenreDto());
        then(authorRepository).should().save(any());
    }

    @Test
    void shouldDeleteGenreById(){
        authorService.deleteGenre("123");
        then(authorRepository).should().deleteById(anyString());
    }
}
