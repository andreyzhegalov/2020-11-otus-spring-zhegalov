package ru.otus.spring.hw.dto;

import org.jetbrains.annotations.NotNull;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.model.Author;

@Getter
@RequiredArgsConstructor
public class AuthorDto {
    private final String id;

    public AuthorDto(@NotNull Author author) {
        this.id = author.getId();
    }
}
