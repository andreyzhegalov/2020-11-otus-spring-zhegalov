package ru.otus.spring.hw.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.dto.AuthorDto;
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;

@RequiredArgsConstructor
@Service
public class IOAuthorService {
    private static final String GET_AUTHOR_ID = "Введите идентификатор автора:";

    private final IOService ioService;

    public AuthorDto getAuthor() {
        ioService.print(GET_AUTHOR_ID);
        final var authorId = ioService.read();
        return new AuthorDto(authorId);
    }

    private void printAuthor(Author author) {
        final StringBuilder sb = new StringBuilder();
        sb.append("id: ").append(author.getId());
        sb.append("; ");
        sb.append("name: ").append(author.getName());
        sb.append("; ");
        final var books = author.getBooks().stream().map(Book::getTitle).collect(Collectors.joining(",")).toString();
        sb.append("books: ");
        sb.append(books);
        ioService.print(sb.toString());
    }

    public void print(List<Author> authors) {
        authors.forEach(this::printAuthor);
    }
}
