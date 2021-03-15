package ru.otus.spring.hw.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Author<T> {
    private T id;
    private String name;
}
