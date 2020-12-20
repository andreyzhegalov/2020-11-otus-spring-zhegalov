package ru.otus.spring.hw.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ru.otus.spring.hw.model.Book;

@SpringBootTest
public class BookDaoJdbcTest {

    @Import(BookDaoJdbs.class)
    @Configuration
    public static class BookDaoInner {
    }

    @Autowired
    private BookDaoJdbs bookDao;

    @Test
    void insertOrUpdateTest() {
        assertThat(bookDao.insertOrUpdate(new Book())).isNotZero();
    }

}
