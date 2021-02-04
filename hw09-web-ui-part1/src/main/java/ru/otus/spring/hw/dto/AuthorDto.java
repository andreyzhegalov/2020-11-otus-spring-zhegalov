package ru.otus.spring.hw.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;

@NoArgsConstructor
@Setter
@Getter
public class AuthorDto {
    private String id;
    @NotBlank
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
