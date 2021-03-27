package ru.otus.spring.hw.dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.model.Genre;

public class BookMapper implements RowMapper<Book<Long>> {
    @Override
    public Book<Long> mapRow(ResultSet rs, int rowNum) throws SQLException {
        final long bookId = rs.getLong("id");
        final String title = rs.getNString("title");
        final var book = new Book<Long>();
        book.setId(bookId);
        book.setTitle(title);

        final long genreId = rs.getLong("genre_id");
        final String genreName = rs.getNString("genre_name");
        final var genre = new Genre<Long>();
        genre.setId(genreId);
        genre.setName(genreName);
        book.setGenre(genre);

        return book;
    }
}
