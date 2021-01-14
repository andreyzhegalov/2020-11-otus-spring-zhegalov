package ru.otus.spring.hw.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;

import ru.otus.spring.hw.model.Genre;

@DataJpaTest
public class GenreRepositoryTest {
    private final static long EXISTED_GENRE_ID = 1L;
    private final static long NOT_EXISTED_GENRE_ID = 3L;
    private final static int GENRE_COUNT = 2;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    void shouldReturnGenreList() {
        var genres = genreRepository.findAll();
        assertThat(genres).isNotNull().hasSize(GENRE_COUNT).allMatch(s -> s != null && !s.getName().equals(""));
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
    void shouldInsertIfGenreIdNotExisted() {
        final var updatedGenre = new Genre("name");
        assertThat(updatedGenre.hasId()).isFalse();

        final var genre = genreRepository.save(updatedGenre);
        em.flush();
        em.flush();

        assertThat(genre.hasId()).isTrue();
        assertThat(genreRepository.findAll()).hasSize(GENRE_COUNT + 1);
    }

    @Test
    void shouldUpdateGenreIfIdExist() {
        final var genre = genreRepository.findById(EXISTED_GENRE_ID).orElseGet(() -> fail("item not exist"));
        genre.setName(genre.getName() + "_modify");
        em.clear();

        genreRepository.save(genre);
        em.flush();
        em.clear();

        assertThat(genreRepository.findById(EXISTED_GENRE_ID)).isPresent().get().isEqualTo(genre);
        assertThat(genreRepository.findAll()).hasSize(GENRE_COUNT);
    }

    @Test
    void deletingAExistingGenreShouldDeleteGenre() {

        genreRepository.findById(EXISTED_GENRE_ID).orElseGet(() -> fail("item not exist"));
        em.clear();

        genreRepository.deleteById(EXISTED_GENRE_ID);
        em.flush();
        em.clear();

        assertThat(genreRepository.findById(EXISTED_GENRE_ID)).isNotPresent();
        assertThat(genreRepository.findAll()).hasSize(GENRE_COUNT - 1);
    }

    @Test
    void deletingANonExistingGenreShouldThrowAnException() {
        assertThatCode(() -> genreRepository.deleteById(NOT_EXISTED_GENRE_ID))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}
