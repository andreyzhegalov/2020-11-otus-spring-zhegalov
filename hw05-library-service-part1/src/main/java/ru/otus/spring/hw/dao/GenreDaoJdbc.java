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
import ru.otus.spring.hw.model.Genre;

@RequiredArgsConstructor
@Component
public class GenreDaoJdbc implements GenreDao {
    private final static String SELECT_BY_ID = "select id,name from genres where id=:id";
    private final static String SELECT_ALL_QUERY = "select id, name from genres";
    private final static String INSERT_QUERY = "insert into genres (name) values (:name)";
    private final static String UPDATE_QUERY = "update genres set name=:name where id=:id";
    private final static String DELETE_QUERY = "delete from genres where id=:id";
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public Optional<Genre> getById(long id) {
        final var result = namedParameterJdbcOperations.query(SELECT_BY_ID, Map.of("id", id), new GenreMapper());
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public List<Genre> getAll() {
        return namedParameterJdbcOperations.query(SELECT_ALL_QUERY, new GenreMapper());
    }

    @Override
    public long insertGenre(Genre genre) {
        final var keyHolder = new GeneratedKeyHolder();
        final var namedParameters = new MapSqlParameterSource().addValue("name", genre.getName());
        final var result = namedParameterJdbcOperations.update(INSERT_QUERY, namedParameters, keyHolder);
        if (result == 0) {
            throw new DaoException(String.format("Genre %s was not added", genre));
        }
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public void updateGenre(Genre genre) {
        final var namedParameters = new MapSqlParameterSource().addValue("id", genre.getId()).addValue("name",
                genre.getName());
        final var result = namedParameterJdbcOperations.update(UPDATE_QUERY, namedParameters);
        if (result == 0) {
            throw new DaoException("No genre found with id " + genre.getId());
        }
    }

    @Override
    public long insertOrUpdate(Genre genre) {
        final var genreFromDb = getById(genre.getId());
        if (genreFromDb.isEmpty()) {
            return insertGenre(genre);
        } else {
            updateGenre(genre);
            return genre.getId();
        }
    }

    @Override
    public void deleteGenre(long id) {
        final var namedParameters = new MapSqlParameterSource().addValue("id", id);
        final var result = namedParameterJdbcOperations.update(DELETE_QUERY, namedParameters);
        if (result == 0) {
            throw new DaoException(String.format("Genre with id %d was not deleted", id));
        }
    }

    private static class GenreMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
            final long id = rs.getLong("id");
            final String name = rs.getNString("name");
            return new Genre(id, name);
        }
    }
}
