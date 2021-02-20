package ru.otus.spring.hw.controllers.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;

@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class AuthorDto {
    private String id;
    @NotBlank(message = "Please provide a author name")
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
