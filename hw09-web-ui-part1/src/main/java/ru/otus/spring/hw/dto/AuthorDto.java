package ru.otus.spring.hw.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.otus.spring.hw.model.Author;

@NoArgsConstructor
@Getter
public class AuthorDto {
    private String id;
    private String name;
    private List<String> booksNameList = new ArrayList<>();

    public AuthorDto(Author author) {
    }
}
