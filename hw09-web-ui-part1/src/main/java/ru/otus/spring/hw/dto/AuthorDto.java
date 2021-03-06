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
    private List<String> booksTitle = new ArrayList<>();

    public AuthorDto(Author author) {
        this.id = author.getId();
        this.name = author.getName();
        this.booksTitle = author.getBooks().stream().map(Book::getTitle).collect(Collectors.toList());
    }

    public Author toEntity() {
        final var author = new Author(getName());
        author.setId(getId());
        return author;
    }
}
