package ru.otus.spring.hw.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
    public long insertOrUpdate(Book book) {
        return 0L;
    }

    @Override
    public List<Book> getAll() {
        return namedParameterJdbcOperations.query("select id from books", new BookMapper());
    }

    private static class BookMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            return new Book(id);
        }
    }

}
