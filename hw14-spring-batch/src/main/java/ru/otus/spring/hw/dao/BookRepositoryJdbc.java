package ru.otus.spring.hw.dao;

import java.util.List;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.dao.mappers.BookMapper;
import ru.otus.spring.hw.model.Book;

@RequiredArgsConstructor
@Component
public class BookRepositoryJdbc {
    private static final String SQL_SELECT_ALL_BOOKS = "select books.id as book_id, title, genres.id as genre_id,"
            + " genres.name as genre_name from " + " books left join genres on books.genre_id=genres.id";

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public List<Book<Long>> findAll() {
        return namedParameterJdbcOperations.query(SQL_SELECT_ALL_BOOKS, new BookMapper());
    }
}
