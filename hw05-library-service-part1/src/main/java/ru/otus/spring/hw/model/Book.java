package ru.otus.spring.hw.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class Book {
    @Setter
    @Getter
    private long id;
    @Getter
    private final String title;
    @Getter
    private final Author author;
    @Getter
    private final Genre genre;

    public Book(String title, Author author, Genre genre) {
        this.id = 0L;
        this.title = title;
        this.author = author;
        this.genre = genre;
    }

    public Book(long id, String title, Author author, Genre genre) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
    }
}
