package ru.otus.spring.hw.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;

@RequiredArgsConstructor
@Service
public class IOBookServiceImpl implements IOBookService {
    private static final String GET_TITLE_MESSAGE = "Введите название книги:";
    private static final String GET_AUTHOR = "Введите автора:";
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
        return new Book(0L, bookTitle, author);
    }

}
