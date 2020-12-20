package ru.otus.spring.hw.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.model.Book;

@RequiredArgsConstructor
@Component
public class BookDaoJdbs implements BookDao {
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public List<Book> getAll() {
        return namedParameterJdbcOperations.query("select id from books", new BookMapper());
    }

    @Override
    public Optional<Book> getById(long id) {
        final var result = namedParameterJdbcOperations.query("select id from books where id=:id ", Map.of("id", id),
                new BookMapper());
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    private static class BookMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            return new Book(id);
        }
    }

    @Override
    public long insertBook(Book book) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void updateBook(Book book) {
        // TODO Auto-generated method stub
    }

    @Override
    public void insertOrUpdate(Book book) {
        // TODO Auto-generated method stub

    }

}
