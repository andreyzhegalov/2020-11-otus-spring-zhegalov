package ru.otus.spring.hw.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

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
    private final static long EXISTED_ID = 1L;
    private final static long NOT_EXISTED_ID = 3L;
    private final static int AUTHOR_COUNT = 2;

    @Autowired
    private GenreDaoJdbc genreDao;

    @Test
    void shouldReturnGenres() {
        assertThat(genreDao.getAll()).hasSize(AUTHOR_COUNT);
    }

    @Test
    void shouldReturnGenreByIdForExistingGenre() {
        assertThat(genreDao.getById(EXISTED_ID).get().getId()).isEqualTo(EXISTED_ID);
    }

    @Test
    void shouldNotReturnGenreByIdForNotExistingGenre() {
        assertThat(genreDao.getById(NOT_EXISTED_ID)).isNotPresent();
    }

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void shouldUpdateIfIdExist() {
        final var initGenre = genreDao.getById(EXISTED_ID).get();
        final var updatedGenre = new Genre(initGenre.getId(), initGenre.getName() + "_modify");

        assertThatCode(() -> genreDao.updateGenre(updatedGenre)).doesNotThrowAnyException();

        assertThat(genreDao.getById(EXISTED_ID).get()).isEqualTo(updatedGenre);
        assertThat(genreDao.getAll().size()).isEqualTo(AUTHOR_COUNT);
    }

    @Test
    void shouldThrowExceptionWhenUpdateNotExistedId() {
        final var updatedGenre = new Genre(NOT_EXISTED_ID, "name");
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
    void insertOrUpdateShouldInsertNewGenre() {
        final var newGenre = new Genre(NOT_EXISTED_ID, "new genre");

        genreDao.insertOrUpdate(newGenre);

        assertThat(genreDao.getById(NOT_EXISTED_ID)).isPresent();
        assertThat(genreDao.getById(NOT_EXISTED_ID).get()).isEqualTo(newGenre);
        assertThat(genreDao.getAll().size()).isEqualTo(AUTHOR_COUNT + 1);
    }

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void insertOrUpdateShouldUpdateExistedGenre() {
        final var existedGenre = genreDao.getById(EXISTED_ID).get();
        final var updatedGenre = new Genre(existedGenre.getId(), existedGenre.getName() + "_modify");

        genreDao.insertOrUpdate(updatedGenre);

        assertThat(genreDao.getById(EXISTED_ID).get()).isEqualTo(updatedGenre);
        assertThat(genreDao.getAll().size()).isEqualTo(AUTHOR_COUNT);
    }

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void deletingAExistingWorkbookShouldDeleteGenre() {
        genreDao.deleteGenre(EXISTED_ID);

        assertThat(genreDao.getById(EXISTED_ID)).isNotPresent();
        assertThat(genreDao.getAll().size()).isEqualTo(AUTHOR_COUNT - 1);
    }

    @Test
    void deletingANonExistingWorkbookShouldThrowAnException() {
        assertThatCode(() -> genreDao.deleteGenre(NOT_EXISTED_ID)).isInstanceOf(DaoException.class);
        assertThat(genreDao.getAll().size()).isEqualTo(AUTHOR_COUNT);
    }

}
