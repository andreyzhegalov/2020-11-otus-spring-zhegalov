package ru.otus.spring.hw.config.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ru.otus.spring.hw.model.Book;

public class BookMapper implements RowMapper<Book> {
    @Override
    public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
        final long bookId = rs.getLong("id");
        final String title = rs.getNString("title");
        final var book = new Book();
        book.setId(bookId);
        book.setTitle(title);
        return book;
    }
}
