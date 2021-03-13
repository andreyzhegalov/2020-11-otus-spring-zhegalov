package ru.otus.spring.hw.dao;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;

import ru.otus.spring.hw.model.Author;

@Component
public class AuthorRepository {
    private final static String SELECT_AUTHOR_WITH_BOOK_ID = "select id, name, ba.book_id as book_id from authors as a "
            + " right join book_author as ba where a.id = ba.author_id";

    private List<AuthorWithBookId> authorList;

    private final static class AuthorWithBookId {
        Author author;
        long bookId;
    }

    public AuthorRepository(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.authorList = namedParameterJdbcOperations.query(SELECT_AUTHOR_WITH_BOOK_ID, (rs, row) -> {
            final long id = rs.getLong("id");
            final String name = rs.getNString("name");
            final long bookId = rs.getLong("book_id");
            final var author = new AuthorWithBookId();
            author.bookId = bookId;
            author.author = new Author(id, name);
            return author;
        });
    }

    public List<Author> getByBookId(long bookId) {
        return this.authorList.stream().filter(author -> author.bookId == bookId).map(author -> author.author)
                .collect(Collectors.toList());
    }

}
