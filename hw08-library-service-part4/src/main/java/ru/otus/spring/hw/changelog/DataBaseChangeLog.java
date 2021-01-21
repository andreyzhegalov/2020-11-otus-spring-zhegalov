package ru.otus.spring.hw.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;

import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Genre;
import ru.otus.spring.hw.repositories.AuthorRepository;
import ru.otus.spring.hw.repositories.GenreRepository;

@ChangeLog(order = "001")
public class DataBaseChangeLog {

    @ChangeSet(order = "001", id = "dropDb", author = "azhegalov", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "initGenre", author = "azhegalov", runAlways = true)
    public void initGenre(GenreRepository repository) {
        repository.save(new Genre("genre1"));
        repository.save(new Genre("genre2"));
        repository.save(new Genre("genre3"));
    }

    @ChangeSet(order = "003", id = "initAuthors", author = "azhegalov", runAlways = true)
    public void initAuthors(AuthorRepository repository) {
        repository.save(new Author("name1"));
        repository.save(new Author("name2"));
        repository.save(new Author("name3"));
    }
}
