package ru.otus.spring.hw.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;

import ru.otus.spring.hw.model.Genre;

@JdbcTest
@Import(GenreDaoJdbc.class)
public class GenreDaoJdbcTest {
    private final static long EXISTED_GENRE_ID = 1L;
    private final static long NOT_EXISTED_GENRE_ID = 3L;
    private final static int AUTHOR_COUNT = 2;

    @Autowired
    private GenreDaoJdbc genreDao;

    @Test
    void shouldReturnGenres() {
        assertThat(genreDao.getAll()).hasSize(AUTHOR_COUNT);
    }

    @Test
    void shouldReturnGenreByIdForExistingGenre() {
        assertThat(genreDao.getById(EXISTED_GENRE_ID).orElseGet(() -> fail("genre not exist")).getId())
                .isEqualTo(EXISTED_GENRE_ID);
    }

    @Test
    void shouldNotReturnGenreByIdForNotExistingGenre() {
        assertThat(genreDao.getById(NOT_EXISTED_GENRE_ID)).isNotPresent();
    }

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void shouldUpdateIfIdExist() {
        final var initGenre = genreDao.getById(EXISTED_GENRE_ID).orElseGet(() -> fail("genre not exist"));
        final var updatedGenre = new Genre(initGenre.getId(), initGenre.getName() + "_modify");

        assertThatCode(() -> genreDao.updateGenre(updatedGenre)).doesNotThrowAnyException();

        assertThat(genreDao.getById(EXISTED_GENRE_ID).orElseGet(() -> fail("genre not exist"))).isEqualTo(updatedGenre);
        assertThat(genreDao.getAll().size()).isEqualTo(AUTHOR_COUNT);
    }

    @Test
    void shouldThrowExceptionWhenUpdateNotExistedId() {
        final var updatedGenre = new Genre(NOT_EXISTED_GENRE_ID, "name");
        assertThatCode(() -> genreDao.updateGenre(updatedGenre)).isInstanceOf(DaoException.class);
        assertThat(genreDao.getAll().size()).isEqualTo(AUTHOR_COUNT);
    }

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void shouldInsertGenre() {
        final var newGenre = new Genre(0L, "new name");

        final var id = genreDao.insertGenre(newGenre);

        assertThat(genreDao.getById(id)).isPresent();
        assertThat(genreDao.getAll().size()).isEqualTo(AUTHOR_COUNT + 1);
    }

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void deletingAExistingWorkbookShouldDeleteGenre() {
        genreDao.deleteGenre(EXISTED_GENRE_ID);

        assertThat(genreDao.getById(EXISTED_GENRE_ID)).isNotPresent();
        assertThat(genreDao.getAll().size()).isEqualTo(AUTHOR_COUNT - 1);
    }

    @Test
    void deletingANonExistingWorkbookShouldThrowAnException() {
        assertThatCode(() -> genreDao.deleteGenre(NOT_EXISTED_GENRE_ID)).isInstanceOf(DaoException.class);
        assertThat(genreDao.getAll().size()).isEqualTo(AUTHOR_COUNT);
    }

}
