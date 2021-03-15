package ru.otus.spring.hw.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Book<T> {
    private T id;
    private String title;
    private List<Author<T>> authors = new ArrayList<>();
    private Genre<T> genre;
}
