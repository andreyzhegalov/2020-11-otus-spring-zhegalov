package ru.otus.spring.hw.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private List<Author> authors = new ArrayList<>();

    private Genre genre;

    public Book(String id, String title, Genre genre, Author... authors) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.authors = Arrays.asList(authors);
    }

    public Book(String title, Genre genre, Author... authors) {
        this.title = title;
        this.genre = genre;
        this.authors = Arrays.asList(authors);
    }

    public void removeAuthor(Author author) {
        authors.remove(author);
    }
}
