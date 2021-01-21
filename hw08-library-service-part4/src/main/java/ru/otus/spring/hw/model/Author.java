package ru.otus.spring.hw.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Author {
    private long id;

    private String name;
}
