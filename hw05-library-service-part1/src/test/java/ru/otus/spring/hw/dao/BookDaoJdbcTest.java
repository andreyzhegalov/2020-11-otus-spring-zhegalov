package ru.otus.spring.hw.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ru.otus.spring.hw.model.Book;

@JdbcTest
@Import(BookDaoJdbs.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class BookDaoJdbcTest {
    private final static long EXISTED_ID = 1L;
    private final static long NOT_EXISTED_ID = 3L;

    @Autowired
    private BookDaoJdbs bookDao;

    @Test
    void shouldReturnBooks() {
        assertThat(bookDao.getAll()).isNotEmpty();
    }

    @Test
    void shouldReturnBookByIdForExistingBook() {
        final long searchingId = EXISTED_ID;
        final var res = bookDao.getById(searchingId);
        assertThat(res).isPresent();
        assertThat(res.get().getId()).isEqualTo(searchingId);
    }

    @Test
    void shouldNotReturnBookByIdForNotExistingBook() {
        assertThat(bookDao.getById(NOT_EXISTED_ID)).isNotPresent();
    }

    @Test
    void shouldUpdateIfIdExist() {
        final long updatedId = EXISTED_ID;
        assertThat(bookDao.getById(updatedId)).isPresent();
        final var updatedBook = bookDao.getById(updatedId);
        // TODO modify book
        assertThatCode(() -> bookDao.updateBook(new Book(updatedId))).doesNotThrowAnyException();
        assertThat(bookDao.getById(updatedId)).isEqualTo(updatedBook);
    }

    @Test
    void shouldInsertBookIfIdNotExist() {
        final long insertedId = NOT_EXISTED_ID;
        assertThat(bookDao.getById(insertedId)).isNotPresent();
        final var newBook = new Book(NOT_EXISTED_ID);
        assertThat(bookDao.insertBook(newBook)).isEqualTo(insertedId);
    }

}
