package ru.otus.spring.hw.changelog;

import java.util.Optional;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;

import org.springframework.data.mongodb.core.MongoOperations;

import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.model.Comment;
import ru.otus.spring.hw.model.Genre;

@ChangeLog(order = "001")
public class DataBaseChangeLog {
    private Genre genre1;
    private Genre genre2;

    private Author author1;
    private Author author2;

    private Book book1;

    @ChangeSet(order = "001", id = "dropDb", author = "azhegalov", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "initGenre", author = "azhegalov", runAlways = true)
    public void initGenre(MongoOperations mongoOperations) {
        genre1 = mongoOperations.insert(new Genre("genre1"));
        genre2 = mongoOperations.insert(new Genre("genre2"));
        mongoOperations.insert(new Genre("genre3"));
    }

    @ChangeSet(order = "003", id = "initAuthors", author = "azhegalov", runAlways = true)
    public void initAuthors(MongoOperations mongoOperations) {
        author1 = mongoOperations.insert(new Author("name1"));
        author2 = mongoOperations.insert(new Author("name2"));
        mongoOperations.insert(new Author("name3"));
    }

    @ChangeSet(order = "004", id = "initBook", author = "azhegalov", runAlways = true)
    public void initBook(MongoOperations mongoOperations) {
        book1 = new Book("book1", genre2, author1, author2);
        mongoOperations.insert(book1);

        Book book2 = new Book("book2", genre1, author1);
        mongoOperations.insert(book2);

        final var loadedAuthor1 = Optional.ofNullable(mongoOperations.findById(author1.getId(), Author.class))
                .orElseThrow();
        loadedAuthor1.addBook(book1);
        loadedAuthor1.addBook(book2);
        mongoOperations.save(loadedAuthor1);

        final var loadedAuthor2 = Optional.ofNullable(mongoOperations.findById(author2.getId(), Author.class))
                .orElseThrow();
        loadedAuthor2.addBook(book1);
        mongoOperations.save(loadedAuthor2);
    }

    @ChangeSet(order = "005", id = "initComments", author = "azhegalov", runAlways = true)
    public void initComments(MongoOperations mongoOperations) {
        final var comment1 = new Comment("comment for book1", book1);
        mongoOperations.insert(comment1);
    }
}
