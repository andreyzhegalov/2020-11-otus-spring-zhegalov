package ru.otus.spring.hw.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.model.Book;

@RequiredArgsConstructor
@Component
public class BookDaoJdbs implements BookDao {
    private final static String SELECT_BY_ID = "select id,title from books where id=:id";
    private final static String SELECT_ALL_QUERY = "select id, title from books";
    private final static String INSERT_QUERY = "insert into books (title) values (:title)";
    private final static String UPDATE_QUERY = "update books set title=:title where id=:id";
    private final static String DELETE_QUERY = "delete from books where id=:id";
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public List<Book> getAll() {
        return namedParameterJdbcOperations.query(SELECT_ALL_QUERY, new BookMapper());
    }

    @Override
    public Optional<Book> getById(long id) {
        final var result = namedParameterJdbcOperations.query(SELECT_BY_ID, Map.of("id", id), new BookMapper());
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public long insertBook(Book book) {
        final var keyHolder = new GeneratedKeyHolder();
        final var namedParameters = new MapSqlParameterSource().addValue("title", book.getTitle());
        final var result = namedParameterJdbcOperations.update(INSERT_QUERY, namedParameters, keyHolder);
        if (result == 0) {
            throw new DaoException("Book not added");
        }
        return (long) keyHolder.getKey();
    }

    @Override
    public void updateBook(Book book) {
        final var namedParameters = new MapSqlParameterSource().addValue("id", book.getId()).addValue("title",
                book.getTitle());
        final var result = namedParameterJdbcOperations.update(UPDATE_QUERY, namedParameters);
        if (result == 0) {
            throw new DaoException("No book found with id " + book.getId());
        }
    }

    @Override
    public void insertOrUpdate(Book book) {
        final var bookFromDb = getById(book.getId());
        if (bookFromDb.isEmpty()) {
            insertBook(book);
        } else {
            updateBook(book);
        }
    }

    @Override
    public void deleteBook(long id) {
        final var namedParameters = new MapSqlParameterSource().addValue("id", id);
        final var result = namedParameterJdbcOperations.update(DELETE_QUERY, namedParameters);
        if (result == 0) {
            throw new DaoException(String.format("Book with id %d was not deleted", id));
        }
    }

    private static class BookMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            final long id = rs.getLong("id");
            final String title = rs.getNString("title");
            return new Book(id, title);
        }
    }

}
