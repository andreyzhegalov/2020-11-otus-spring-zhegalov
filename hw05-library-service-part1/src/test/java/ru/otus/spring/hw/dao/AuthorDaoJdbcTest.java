package ru.otus.spring.hw.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ru.otus.spring.hw.model.dto.AuthorDto;

@JdbcTest
@Import(AuthorDaoJdbc.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class AuthorDaoJdbcTest {
    private final static long EXISTED_ID = 1L;
    private final static long NOT_EXISTED_ID = 3L;
    private final static int AUTHOR_COUNT = 2;

    @Autowired
    private AuthorDaoJdbc authorDao;

    @Test
    void shouldReturnAuthors() {
        assertThat(authorDao.getAll()).hasSize(AUTHOR_COUNT);
    }

    @Test
    void shouldReturnAuthorByIdForExistingAuthor() {
        assertThat(authorDao.getById(EXISTED_ID).get().getId()).isEqualTo(EXISTED_ID);
    }

    @Test
    void shouldNotReturnAuthorByIdForNotExistingAuthor() {
        assertThat(authorDao.getById(NOT_EXISTED_ID)).isNotPresent();
    }

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void shouldUpdateIfIdExist() {
        final var initAuthor = authorDao.getById(EXISTED_ID).get();
        final var updatedAuthor = new AuthorDto(initAuthor.getId(), initAuthor.getName() + "_modify");

        assertThatCode(() -> authorDao.updateAuthor(updatedAuthor)).doesNotThrowAnyException();

        assertThat(authorDao.getById(EXISTED_ID).get()).isEqualTo(updatedAuthor);
        assertThat(authorDao.getAll().size()).isEqualTo(AUTHOR_COUNT);
    }

    @Test
    void shouldThrowExceptionWhenUpdateNotExistedId() {
        final var updatedAuthor = new AuthorDto(NOT_EXISTED_ID, "name");
        assertThatCode(() -> authorDao.updateAuthor(updatedAuthor)).isInstanceOf(DaoException.class);
        assertThat(authorDao.getAll().size()).isEqualTo(AUTHOR_COUNT);
    }

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void shouldInsertAuthor() {
        final var newAuthor = new AuthorDto("new name");

        final var id = authorDao.insertAuthor(newAuthor);

        assertThat(authorDao.getById(id)).isPresent();
        assertThat(authorDao.getAll().size()).isEqualTo(AUTHOR_COUNT + 1);
    }

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void insertOrUpdateShouldInsertNewAuthor() {
        final var newAuthor = new AuthorDto(NOT_EXISTED_ID, "new author");

        authorDao.insertOrUpdate(newAuthor);

        assertThat(authorDao.getById(NOT_EXISTED_ID)).isPresent();
        assertThat(authorDao.getById(NOT_EXISTED_ID).get()).isEqualTo(newAuthor);
        assertThat(authorDao.getAll().size()).isEqualTo(AUTHOR_COUNT + 1);
    }

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void insertOrUpdateShouldUpdateExistedAuthor() {
        final var existedAuthor = authorDao.getById(EXISTED_ID).get();
        final var updatedAuthor = new AuthorDto(existedAuthor.getId(), existedAuthor.getName() + "_modify");

        authorDao.insertOrUpdate(updatedAuthor);

        assertThat(authorDao.getById(EXISTED_ID).get()).isEqualTo(updatedAuthor);
        assertThat(authorDao.getAll().size()).isEqualTo(AUTHOR_COUNT);
    }

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void deletingAExistingWorkbookShouldDeleteAuthor() {
        authorDao.deleteAuthor(EXISTED_ID);

        assertThat(authorDao.getById(EXISTED_ID)).isNotPresent();
        assertThat(authorDao.getAll().size()).isEqualTo(AUTHOR_COUNT - 1);
    }

    @Test
    void deletingANonExistingWorkbookShouldThrowAnException() {
        assertThatCode(() -> authorDao.deleteAuthor(NOT_EXISTED_ID)).isInstanceOf(DaoException.class);
        assertThat(authorDao.getAll().size()).isEqualTo(AUTHOR_COUNT);
    }
}
