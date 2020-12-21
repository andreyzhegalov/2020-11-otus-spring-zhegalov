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

import ru.otus.spring.hw.model.dto.BookDto;

@JdbcTest
@Import(BookDaoJdbs.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class BookDaoJdbcTest {
    private final static long EXISTED_ID = 1L;
    private final static long NOT_EXISTED_ID = 3L;
    private final static int BOOK_COUNT = 2;
    private final static long EXISTED_AUTHOR_ID = 1L;

    @Autowired
    private BookDaoJdbs bookDao;

    @Test
    void shouldReturnBooks() {
        assertThat(bookDao.getAll()).hasSize(BOOK_COUNT);
    }

    @Test
    void shouldReturnBookByIdForExistingBook() {
        assertThat(bookDao.getById(EXISTED_ID).get().getId()).isEqualTo(EXISTED_ID);
    }

    @Test
    void shouldNotReturnBookByIdForNotExistingBook() {
        assertThat(bookDao.getById(NOT_EXISTED_ID)).isNotPresent();
    }

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void shouldUpdateIfIdExist() {
        final var initBook = bookDao.getById(EXISTED_ID).get();
        final var updatedBook = new BookDto(initBook.getId(), initBook.getTitle() + "_modify", initBook.getAuthorId());

        assertThatCode(() -> bookDao.updateBook(updatedBook)).doesNotThrowAnyException();

        assertThat(bookDao.getById(EXISTED_ID).get()).isEqualTo(updatedBook);
        assertThat(bookDao.getAll().size()).isEqualTo(BOOK_COUNT);
    }

    @Test
    void shouldThrowExceptionWhenUpdateNotExistedId() {
        final var updatedBook = new BookDto(NOT_EXISTED_ID, "title", EXISTED_AUTHOR_ID);
        assertThatCode(() -> bookDao.updateBook(updatedBook)).isInstanceOf(DaoException.class);
        assertThat(bookDao.getAll().size()).isEqualTo(BOOK_COUNT);
    }

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void shouldInsertBook() {
        final var newBook = new BookDto("new title", EXISTED_AUTHOR_ID);

        final var id = bookDao.insertBook(newBook);

        assertThat(bookDao.getById(id)).isPresent();
        assertThat(bookDao.getAll().size()).isEqualTo(BOOK_COUNT + 1);
    }

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void insertOrUpdateShouldInsertNewBook() {
        final var newBook = new BookDto(NOT_EXISTED_ID, "new book", EXISTED_AUTHOR_ID);

        bookDao.insertOrUpdate(newBook);

        assertThat(bookDao.getById(NOT_EXISTED_ID)).isPresent();
        assertThat(bookDao.getById(NOT_EXISTED_ID).get()).isEqualTo(newBook);
        assertThat(bookDao.getAll().size()).isEqualTo(BOOK_COUNT + 1);
    }

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void insertOrUpdateShouldUpdateExistedBook() {
        final var existedBook = bookDao.getById(EXISTED_ID).get();
        final var updatedBook = new BookDto(existedBook.getId(), existedBook.getTitle() + "_modify", existedBook.getAuthorId());

        bookDao.insertOrUpdate(updatedBook);

        assertThat(bookDao.getById(EXISTED_ID).get()).isEqualTo(updatedBook);
        assertThat(bookDao.getAll().size()).isEqualTo(BOOK_COUNT);
    }

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void deletingAExistingWorkbookShouldDeleteBook() {
        bookDao.deleteBook(EXISTED_ID);

        assertThat(bookDao.getById(EXISTED_ID)).isNotPresent();
        assertThat(bookDao.getAll().size()).isEqualTo(BOOK_COUNT - 1);
    }

    @Test
    void deletingANonExistingWorkbookShouldThrowAnException() {
        assertThatCode(() -> bookDao.deleteBook(NOT_EXISTED_ID)).isInstanceOf(DaoException.class);
        assertThat(bookDao.getAll().size()).isEqualTo(BOOK_COUNT);
    }
}
