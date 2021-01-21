package ru.otus.spring.hw.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.dto.BookDto;

@RequiredArgsConstructor
@Service
public class IOBookService {
    private static final String GET_TITLE_MESSAGE = "Введите название книги:";
    private static final String GET_AUTHOR_ID = "Введите идентификатор автора:";
    private static final String GET_GENRE_ID = "Введите идентификатор жанра:";
    private final IOService ioService;

    public void print(List<String> books) {
        books.forEach(b -> ioService.print(b.toString()));
    }

    public BookDto getBook() {
        ioService.print(GET_TITLE_MESSAGE);
        final var bookTitle = ioService.read();
        ioService.print(GET_AUTHOR_ID);
        final var authorId = ioService.read();
        ioService.print(GET_GENRE_ID);
        final var genreId = ioService.read();
        final var bookDto = new BookDto(bookTitle, genreId);
        bookDto.getAuthorIds().add(authorId);
        return bookDto;
    }
}
