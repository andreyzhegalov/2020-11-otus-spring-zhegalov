package ru.otus.spring.hw.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Book {
    private final static long NOT_EXISTED_ID = 0L;

    private long id = NOT_EXISTED_ID;
    private String title;
    private List<Author> authors = new ArrayList<>();
    private Genre genre;

    public Book(long id, String title, Genre genre) {
        this.id = id;
        this.title = title;
        this.genre = genre;
    }
}
