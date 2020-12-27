package ru.otus.spring.hw.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import ru.otus.spring.hw.dao.DaoException;
import ru.otus.spring.hw.model.Genre;

@DataJpaTest
@Import(GenreRepositoryJpa.class)
public class GenreRepositoryJpaTest {
    private final static long EXISTED_GENRE_ID = 1L;
    private final static long NOT_EXISTED_GENRE_ID = 3L;
    private final static int GENRE_COUNT = 2;

    @Autowired
    private GenreRepositoryJpa genreRepository;

    @Test
    void shouldReturnGenreList() {
        var genres = genreRepository.findAll();
        assertThat(genres).isNotNull().hasSize(GENRE_COUNT).allMatch(s -> !s.getName().equals(""));
    }

    @Test
    void shouldReturnGenreByIdWhenGenreExisted() {
        assertThat(genreRepository.findById(EXISTED_GENRE_ID)).isPresent().get().extracting("id")
                .isEqualTo(EXISTED_GENRE_ID);
    }

    @Test
    void shouldNotReturnGenreByIdForNotExistingGenre() {
        assertThat(genreRepository.findById(NOT_EXISTED_GENRE_ID)).isNotPresent();
    }

    @Test
    void shouldUpdateGenreIfIdExist() {
        final var initGenre = genreRepository.findById(EXISTED_GENRE_ID).orElseGet(() -> fail("genre not exist"));
        final var updatedGenre = new Genre(initGenre.getId(), initGenre.getName() + "_modify");

        assertThatCode(() -> genreRepository.save(updatedGenre)).doesNotThrowAnyException();

        assertThat(genreRepository.findById(EXISTED_GENRE_ID)).isPresent().get().isEqualTo(updatedGenre);
        assertThat(genreRepository.findAll()).hasSize(GENRE_COUNT);
    }

    @Test
    void shouldInsertIfGenreIdNotExisted() {
        final var updatedGenre = new Genre(NOT_EXISTED_GENRE_ID, "name");
        final var genre = genreRepository.save(updatedGenre);
        assertThat(genreRepository.findAll()).hasSize(GENRE_COUNT + 1);
        assertThat(genre).isEqualTo(updatedGenre);
    }

    @Test
    void deletingAExistingWorkbookShouldDeleteGenre() {
        genreRepository.remove(EXISTED_GENRE_ID);

        assertThat(genreRepository.findById(EXISTED_GENRE_ID)).isNotPresent();
        assertThat(genreRepository.findAll()).hasSize(GENRE_COUNT - 1);
    }

    @Test
    void deletingANonExistingWorkbookShouldThrowAnException() {
        assertThatCode(() -> genreRepository.remove(NOT_EXISTED_GENRE_ID)).isInstanceOf(DaoException.class);
        assertThat(genreRepository.findAll()).hasSize(GENRE_COUNT);
    }
}
