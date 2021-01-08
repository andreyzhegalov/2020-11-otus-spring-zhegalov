package ru.otus.spring.hw.dto;

import org.jetbrains.annotations.NotNull;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.otus.spring.hw.model.Author;

@ToString
@Getter
@RequiredArgsConstructor
public class AuthorDto {
    private final long id;

    public AuthorDto(@NotNull Author author) {
        this.id = author.getId();
    }
}
