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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ru.otus.spring.hw.dto.BookDto;

@JdbcTest
@Import(BookDaoJdbs.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class BookDaoJdbcTest {
    private final static long EXISTED_BOOK_ID = 1L;
    private final static long NOT_EXISTED_BOOK_ID = 3L;
    private final static int BOOK_COUNT = 2;
    private final static long EXISTED_AUTHOR_ID = 1L;
    private final static long EXISTED_GENRE_ID = 1L;

    @Autowired
    private BookDaoJdbs bookDao;

    @Test
    void shouldReturnBooks() {
        assertThat(bookDao.getAll()).hasSize(BOOK_COUNT);
    }

    @Test
    void shouldReturnBooksNew() {
        assertThat(bookDao.getAll()).hasSize(BOOK_COUNT).allMatch(s -> !s.getTitle().equals(""))
                .allMatch(s -> s.getAuthor() != null).allMatch(s -> s.getGenre() != null);
    }

    @Test
    void shouldReturnBookByIdForExistingBook() {
        assertThat(bookDao.getById(EXISTED_BOOK_ID).orElseGet(() -> fail("book not exist")).getId())
                .isEqualTo(EXISTED_BOOK_ID);
    }

    @Test
    void shouldNotReturnBookByIdForNotExistingBook() {
        assertThat(bookDao.getById(NOT_EXISTED_BOOK_ID)).isNotPresent();
    }

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void shouldUpdateIfIdExist() {
        final var initBook = bookDao.getById(EXISTED_BOOK_ID).orElseGet(() -> fail("book not exist"));
        final var initBookDto = new BookDto(initBook);
        final var updatedBookDto = new BookDto(initBookDto.getId(), initBookDto.getTitle() + "_modify",
                initBookDto.getAuthorId(), initBookDto.getGenreId());

        assertThatCode(() -> bookDao.updateBook(updatedBookDto)).doesNotThrowAnyException();

        final var afterUpdateDto = new BookDto(
                bookDao.getById(EXISTED_BOOK_ID).orElseGet(() -> fail("book not exist")));
        assertThat(afterUpdateDto).isEqualTo(updatedBookDto);
        assertThat(bookDao.getAll().size()).isEqualTo(BOOK_COUNT);
    }

    @Test
    void shouldThrowExceptionWhenUpdateNotExistedId() {
        final var updatedBook = new BookDto(NOT_EXISTED_BOOK_ID, "title", EXISTED_AUTHOR_ID, EXISTED_GENRE_ID);
        assertThatCode(() -> bookDao.updateBook(updatedBook)).isInstanceOf(DaoException.class);
        assertThat(bookDao.getAll().size()).isEqualTo(BOOK_COUNT);
    }

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void shouldInsertBook() {
        final var newBook = new BookDto("new title", EXISTED_AUTHOR_ID, EXISTED_GENRE_ID);

        final var id = bookDao.insertBook(newBook);

        assertThat(bookDao.getById(id)).isPresent();
        assertThat(bookDao.getAll().size()).isEqualTo(BOOK_COUNT + 1);
    }

    @DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
    @Test
    void deletingAExistingWorkbookShouldDeleteBook() {
        bookDao.deleteBook(EXISTED_BOOK_ID);

        assertThat(bookDao.getById(EXISTED_BOOK_ID)).isNotPresent();
        assertThat(bookDao.getAll().size()).isEqualTo(BOOK_COUNT - 1);
    }

    @Test
    void deletingANonExistingWorkbookShouldThrowAnException() {
        assertThatCode(() -> bookDao.deleteBook(NOT_EXISTED_BOOK_ID)).isInstanceOf(DaoException.class);
        assertThat(bookDao.getAll().size()).isEqualTo(BOOK_COUNT);
    }
}
