package ru.otus.spring.hw.dao;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Autowired
    private BookDaoJdbs bookDao;

    @Test
    void shouldReturnBooks(){
        assertThat(bookDao.getAll()).isNotEmpty();
    }

    @Test
    void insertOrUpdateTest() {
        assertThat(bookDao.insertOrUpdate(new Book(1L))).isNotZero();
    }

}
