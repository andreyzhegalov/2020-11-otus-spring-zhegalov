package ru.otus.spring.hw.model.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class AuthorDto {
    @Getter
    private final long id;
    @Getter
    private final String name;

    public AuthorDto(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public AuthorDto(String name) {
        this.id = 0;
        this.name = name;
    }
}
