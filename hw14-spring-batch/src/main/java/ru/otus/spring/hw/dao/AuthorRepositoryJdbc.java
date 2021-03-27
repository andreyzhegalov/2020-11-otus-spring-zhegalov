package ru.otus.spring.hw.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;

import ru.otus.spring.hw.model.Author;

@Component
public class AuthorRepositoryJdbc {
    private final static String SELECT_AUTHOR_WITH_BOOK_ID = "select id, name, ba.book_id as book_id from authors as a "
            + " right join book_author as ba where a.id = ba.author_id";

    private final Map<Long, List<Author<Long>>> authorByBookIdMap = new HashMap<>();

    public AuthorRepositoryJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        namedParameterJdbcOperations.query(SELECT_AUTHOR_WITH_BOOK_ID, (rs, row) -> {
            final var id = rs.getLong("id");
            final var name = rs.getNString("name");
            final var bookId = rs.getLong("book_id");

            final var author = new Author<Long>();
            author.setId(id);
            author.setName(name);

            var authorList = authorByBookIdMap.computeIfAbsent(bookId, k -> new ArrayList<>());
            authorList.add(author);
            return null;
        });
    }

    public List<Author<Long>> getByBookId(long bookId) {
        final var authorList = authorByBookIdMap.get(bookId);
        return authorList == null ? Collections.emptyList() : authorList;
    }
}
