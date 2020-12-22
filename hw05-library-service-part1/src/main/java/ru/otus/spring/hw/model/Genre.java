package ru.otus.spring.hw.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class Genre {
    @Getter
    private final long id;
    @Getter
    private final String name;

    public Genre(String name) {
        this.id = 0L;
        this.name = name;
    }

    public Genre(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
