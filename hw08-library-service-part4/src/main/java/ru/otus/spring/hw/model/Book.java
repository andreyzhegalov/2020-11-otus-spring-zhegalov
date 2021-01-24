package ru.otus.spring.hw.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "books")
@NoArgsConstructor
@Data
public class Book {
    @Id
    private String id;

    private String title;

    @Setter
    private Set<Author> authors = new HashSet<>();

    private Genre genre;

    public Book(String id, String title, Genre genre) {
        this.id = id;
        this.title = title;
        this.genre = genre;
    }

    public Book(String title, Genre genre) {
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
