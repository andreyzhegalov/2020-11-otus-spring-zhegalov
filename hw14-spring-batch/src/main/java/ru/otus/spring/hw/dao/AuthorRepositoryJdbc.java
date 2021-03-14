package ru.otus.spring.hw.dao;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;

import ru.otus.spring.hw.model.Author;

@Component
public class AuthorRepositoryJdbc {
    private final static String SELECT_AUTHOR_WITH_BOOK_ID = "select id, name, ba.book_id as book_id from authors as a "
            + " right join book_author as ba where a.id = ba.author_id";

    private final List<AuthorWithBookId> authorList;

    private final static class AuthorWithBookId {
        Author<Long> author;
        long bookId;
    }

    public AuthorRepositoryJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.authorList = namedParameterJdbcOperations.query(SELECT_AUTHOR_WITH_BOOK_ID, (rs, row) -> {
            final var id = rs.getLong("id");
            final var name = rs.getNString("name");
            final var bookId = rs.getLong("book_id");
            final var author = new AuthorWithBookId();

            author.bookId = bookId;
            author.author = new Author<Long>();
            author.author.setId(id);
            author.author.setName(name);
            return author;
        });
    }

    public List<Author<Long>> getByBookId(long bookId) {
        return this.authorList.stream().filter(author -> author.bookId == bookId).map(author -> author.author)
                .collect(Collectors.toList());
    }

}
