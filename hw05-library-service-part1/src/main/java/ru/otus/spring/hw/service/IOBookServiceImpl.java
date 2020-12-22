package ru.otus.spring.hw.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.model.Genre;

@RequiredArgsConstructor
@Service
public class IOBookServiceImpl implements IOBookService {
    private static final String GET_TITLE_MESSAGE = "Введите название книги:";
    private static final String GET_AUTHOR = "Введите автора:";
    private static final String GET_GENRE = "Введите жанр:";
    private final IOService ioService;

    @Override
    public void printBooks(List<Book> books) {
        books.forEach(b -> ioService.print(b.toString()));
    }

    @Override
    public Book getBook() {
        ioService.print(GET_TITLE_MESSAGE);
        final var bookTitle = ioService.read();
        ioService.print(GET_AUTHOR);
        final var authorName = ioService.read();
        final var author = new Author(authorName);
        ioService.print(GET_GENRE);
        final var genreName = ioService.read();
        final var genre = new Genre(genreName);
        return new Book(0L, bookTitle, author, genre);
    }

}
