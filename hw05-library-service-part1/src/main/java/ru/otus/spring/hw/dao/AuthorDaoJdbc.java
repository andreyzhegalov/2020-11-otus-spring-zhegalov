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

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.model.Author;

@RequiredArgsConstructor
public class AuthorDaoJdbc implements AuthorDao {
    private final static String SELECT_BY_ID = "select id,name from authors where id=:id";
    private final static String SELECT_ALL_QUERY = "select id, name from authors";
    private final static String INSERT_QUERY = "insert into authors (name) values (:name)";
    private final static String UPDATE_QUERY = "update authors set name=:name where id=:id";
    private final static String DELETE_QUERY = "delete from authors where id=:id";
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public Optional<Author> getById(long id) {
        final var result = namedParameterJdbcOperations.query(SELECT_BY_ID, Map.of("id", id), new AuthorMapper());
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public List<Author> getAll() {
        return namedParameterJdbcOperations.query(SELECT_ALL_QUERY, new AuthorMapper());
    }

    @Override
    public long insertAuthor(Author author) {
        final var keyHolder = new GeneratedKeyHolder();
        final var namedParameters = new MapSqlParameterSource().addValue("name", author.getName());
        final var result = namedParameterJdbcOperations.update(INSERT_QUERY, namedParameters, keyHolder);
        if (result == 0) {
            throw new DaoException("Author not added");
        }
        return (long) keyHolder.getKey();
    }

    @Override
    public void updateAuthor(Author author) {
        final var namedParameters = new MapSqlParameterSource().addValue("id", author.getId()).addValue("name",
                author.getName());
        final var result = namedParameterJdbcOperations.update(UPDATE_QUERY, namedParameters);
        if (result == 0) {
            throw new DaoException("No author found with id " + author.getId());
        }
    }

    @Override
    public void insertOrUpdate(Author author) {
        final var authorFromDb = getById(author.getId());
        if (authorFromDb.isEmpty()) {
            insertAuthor(author);
        } else {
            updateAuthor(author);
        }
    }

    @Override
    public void deleteAuthor(long id) {
        final var namedParameters = new MapSqlParameterSource().addValue("id", id);
        final var result = namedParameterJdbcOperations.update(DELETE_QUERY, namedParameters);
        if (result == 0) {
            throw new DaoException(String.format("Author with id %d was not deleted", id));
        }
    }

    private static class AuthorMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
            final long id = rs.getLong("id");
            final String name = rs.getNString("name");
            return new Author(id, name);
        }
    }
}
