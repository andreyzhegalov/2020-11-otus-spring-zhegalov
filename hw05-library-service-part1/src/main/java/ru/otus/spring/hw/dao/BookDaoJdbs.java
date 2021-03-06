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
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.dto.BookDto;
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.model.Genre;

@RequiredArgsConstructor
@Repository
public class BookDaoJdbs implements BookDao {
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public List<Book> getAll() {
        return namedParameterJdbcOperations.query("select " + "books.id as book_id," + "title,"
                + "authors.id as author_id," + "authors.name as author_name," + "genres.id as genre_id,"
                + "genres.name as genre_name " + "from  books " + "left join authors on books.author_id=authors.id "
                + "left join genres  on books.genre_id=genres.id", new BookMapper());
    }

    @Override
    public Optional<Book> getById(long id) {
        final var result = namedParameterJdbcOperations.query(
                "select " + "books.id as book_id," + "title," + "authors.id as author_id,"
                        + "authors.name as author_name," + "genres.id as genre_id," + "genres.name as genre_name "
                        + "from  books " + "left join authors on books.author_id=authors.id "
                        + "left join genres  on books.genre_id=genres.id" + " where books.id=:id",
                Map.of("id", id), new BookMapper());
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public long insertBook(BookDto book) {
        final var mapSqlParameter = new MapSqlParameterSource().addValues(makeBookDtoParameterMap(book));
        final var keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcOperations.update(
                "insert into books (title, author_id, genre_id) values (:title, :author_id, :genre_id)",
                mapSqlParameter, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public void updateBook(BookDto book) {
        final var mapSqlParameter = new MapSqlParameterSource().addValues(makeBookDtoParameterMap(book));
        final var result = namedParameterJdbcOperations.update(
                "update books set title=:title, author_id=:author_id, genre_id=:genre_id where id=:id",
                mapSqlParameter);
        if (result == 0) {
            throw new DaoException("No book found with id " + book.getId());
        }
    }

    @Override
    public void deleteBook(long id) {
        final var namedParameters = new MapSqlParameterSource().addValue("id", id);
        final var result = namedParameterJdbcOperations.update("delete from books where id=:id", namedParameters);
        if (result == 0) {
            throw new DaoException(String.format("Book with id %d was not deleted", id));
        }
    }

    private final Map<String, Object> makeBookDtoParameterMap(BookDto book) {
        return Map.of("id", book.getId(), "title", book.getTitle(), "author_id", book.getAuthorId(), "genre_id",
                book.getGenreId());
    }

    private static class BookMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            final long book_id = rs.getLong("book_id");
            final String title = rs.getNString("title");
            final long authorId = rs.getLong("author_id");
            final String authorName = rs.getNString("author_name");
            final long genreId = rs.getLong("genre_id");
            final String genreName = rs.getNString("genre_name");

            final var author = new Author(authorId, authorName);
            final var genre = new Genre(genreId, genreName);
            return new Book(book_id, title, author, genre);
        }
    }

}
