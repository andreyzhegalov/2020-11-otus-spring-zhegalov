package ru.otus.spring.hw.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Book {
    private long id;
    private final String title;
    private final Author author;
    private final Genre genre;
}
