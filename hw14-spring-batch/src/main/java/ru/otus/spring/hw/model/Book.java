package ru.otus.spring.hw.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Book<T> {
    private T id;
    private String title;
    private List<Author<T>> authors = new ArrayList<>();
    private Genre<T> genre;
}
