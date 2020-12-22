package ru.otus.spring.hw.service.io;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.model.Genre;

@RequiredArgsConstructor
@Service
public class IOBookService implements IOModelService<Book> {
    private static final String GET_TITLE_MESSAGE = "Введите название книги:";
    private static final String GET_AUTHOR_ID = "Введите идентификатор автора:";
    private static final String GET_GENRE_ID = "Введите идентификатор жанра:";
    private final IOService ioService;

    @Override
    public void print(List<Book> books) {
        books.forEach(b -> ioService.print(b.toString()));
    }

    @Override
    public Book get() {
        ioService.print(GET_TITLE_MESSAGE);
        final var bookTitle = ioService.read();
        ioService.print(GET_AUTHOR_ID);
        final var authorId = Long.valueOf(ioService.read());
        final var author = new Author(authorId, "");
        ioService.print(GET_GENRE_ID);
        final var genreId = Long.valueOf(ioService.read());
        final var genre = new Genre(genreId, "");
        return new Book(bookTitle, author, genre);
    }

}
