package ru.otus.spring.hw.model;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Book {
    private long id;

    private String title;

    private final Set<Author> authors = new HashSet<>();

    private Genre genre;

    public Book(long id, String title, Genre genre) {
        this.id = id;
        this.title = title;
        this.genre = genre;
    }

    public void addAuthor(Author author) {
        authors.add(author);
    }

    public void removeAuthor(Author author) {
        authors.remove(author);
    }
}
