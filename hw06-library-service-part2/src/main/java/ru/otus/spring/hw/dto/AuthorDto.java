package ru.otus.spring.hw.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.model.Author;

@Getter
@RequiredArgsConstructor
public class AuthorDto {
    private final long id;

    public AuthorDto(Author author) {
        this.id = author.getId();
    }
}
