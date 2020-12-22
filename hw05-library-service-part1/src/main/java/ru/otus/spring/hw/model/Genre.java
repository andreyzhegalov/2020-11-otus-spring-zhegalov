package ru.otus.spring.hw.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class Genre {
    @Getter
    private long id;
    @Getter
    private String name;

    public Genre(String name) {
        this.id = 0;
        this.name = name;
    }

    public Genre(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
