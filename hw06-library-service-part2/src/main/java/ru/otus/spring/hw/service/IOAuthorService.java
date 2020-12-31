package ru.otus.spring.hw.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.dto.AuthorDto;
import ru.otus.spring.hw.model.Author;

@RequiredArgsConstructor
@Service
public class IOAuthorService {
    private static final String GET_AUTHOR_ID = "Введите идентификатор автора:";

    private final IOService ioService;

    public AuthorDto getAuthor() {
        ioService.print(GET_AUTHOR_ID);
        final var authorId = Long.parseLong(ioService.read());
        return new AuthorDto(authorId);
    }

    public void print(List<Author> authors) {
        authors.forEach(b -> ioService.print(b.toString()));
    }
}
