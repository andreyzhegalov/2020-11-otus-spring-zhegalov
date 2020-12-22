package ru.otus.spring.hw.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.dao.dto.BookDto;

@RequiredArgsConstructor
@Component
public class BookDaoJdbs implements BookDao {
    private final static String SELECT_BY_ID = "select id,title, author_id, genre_id from books where id=:id";
    private final static String SELECT_ALL_QUERY = "select id, title, author_id, genre_id from books";
    private final static String INSERT_QUERY = "insert into books (title, author_id, genre_id) values (:title, :author_id, :genre_id)";
    private final static String UPDATE_QUERY = "update books set title=:title, author_id=:author_id, genre_id=:genre_id where id=:id";
    private final static String DELETE_QUERY = "delete from books where id=:id";
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public List<BookDto> getAll() {
        return namedParameterJdbcOperations.query(SELECT_ALL_QUERY, new BookMapper());
    }

    @Override
    public Optional<BookDto> getById(long id) {
        final var result = namedParameterJdbcOperations.query(SELECT_BY_ID, Map.of("id", id), new BookMapper());
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public long insertBook(BookDto book) {
        final var mapSqlParameter = new MapSqlParameterSource().addValues(makeParameterMap(book));
        final var keyHolder = new GeneratedKeyHolder();
        final var result = namedParameterJdbcOperations.update(INSERT_QUERY, mapSqlParameter, keyHolder);
        if (result == 0) {
            throw new DaoException("Book not added");
        }
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public void updateBook(BookDto book) {
        final var mapSqlParameter = new MapSqlParameterSource().addValues(makeParameterMap(book));
        final var result = namedParameterJdbcOperations.update(UPDATE_QUERY, mapSqlParameter);
        if (result == 0) {
            throw new DaoException("No book found with id " + book.getId());
        }
    }

    @Override
    public void insertOrUpdate(BookDto book) {
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

    private static class BookMapper implements RowMapper<BookDto> {
        @Override
        public BookDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            final long id = rs.getLong("id");
            final String title = rs.getNString("title");
            final long authorId = rs.getLong("author_id");
            final long genreId = rs.getLong("genre_id");
            return new BookDto(id, title, authorId, genreId);
        }
    }

    private final Map<String, Object> makeParameterMap(BookDto book) {
        return Map.of("id", book.getId(), "title", book.getTitle(), "author_id", book.getAuthorId(), "genre_id",
                book.getGenreId());
    }
}
