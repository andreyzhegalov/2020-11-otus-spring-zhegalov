package ru.otus.spring.hw.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;

import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.model.Genre;
import ru.otus.spring.hw.repositories.AuthorRepository;
import ru.otus.spring.hw.repositories.BookRepository;
import ru.otus.spring.hw.repositories.GenreRepository;

@ChangeLog(order = "001")
public class DataBaseChangeLog {
    private Genre genre1;
    private Genre genre2;

    private Author author1;
    private Author author2;

    private Book book1;
    private Book book2;

    @ChangeSet(order = "001", id = "dropDb", author = "azhegalov", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "initGenre", author = "azhegalov", runAlways = true)
    public void initGenre(GenreRepository repository) {
        genre1 = repository.save(new Genre("genre1"));
        genre2 = repository.save(new Genre("genre2"));
        repository.save(new Genre("genre3"));
    }

    @ChangeSet(order = "003", id = "initAuthors", author = "azhegalov", runAlways = true)
    public void initAuthors(AuthorRepository repository) {
        author1 = repository.save(new Author("name1"));
        author2 = repository.save(new Author("name2"));
        repository.save(new Author("name3"));
    }

    @ChangeSet(order = "004", id = "initBook", author = "azhegalov", runAlways = true)
    public void initBook(BookRepository repository) {
        book1 = new Book("book1", genre2);
        book1.addAuthor(author1);
        book1.addAuthor(author2);
        repository.save(book1);

        book2 = new Book("book2", genre1);
        book2.addAuthor(author1);
        repository.save(book2);
    }
}
