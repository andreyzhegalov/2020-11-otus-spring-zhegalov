package ru.otus.spring.hw.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;

@NoArgsConstructor
@Getter
public class AuthorDto {
    private String id;
    private String name;
    private List<String> booksTitleList = new ArrayList<>();

    public String getBooksTitle() {
        return String.join(",", booksTitleList);
    }

    public AuthorDto(Author author) {
        this.id = author.getId();
        this.name = author.getName();
        this.booksTitleList = author.getBooks().stream().map(Book::getTitle).collect(Collectors.toList());
    }
}
