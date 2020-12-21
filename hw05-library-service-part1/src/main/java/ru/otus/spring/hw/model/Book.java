package ru.otus.spring.hw.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class Book {
    @Getter
    private final long id;
    @Getter
    private final String title;
    @Getter
    private final long authorId;

    public Book(String title, long authorId) {
        this.id = 0;
        this.title = title;
        this.authorId = authorId;
    }

    public Book(long id, String title, long authorId) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
    }
}
