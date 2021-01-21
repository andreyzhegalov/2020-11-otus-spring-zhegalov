package ru.otus.spring.hw.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@NoArgsConstructor
@Data
public class Book {
    private String id;

    private String title;

    private final Set<Author> authors = new HashSet<>();

    private Genre genre;

    public Book(String id, String title, Genre genre) {
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
