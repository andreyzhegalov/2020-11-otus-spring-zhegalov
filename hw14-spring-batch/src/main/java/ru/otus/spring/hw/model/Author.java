package ru.otus.spring.hw.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Author<T> {
    private T id;
    private String name;
}
