package ru.otus.spring.hw.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.dto.BookDto;
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;

@RequiredArgsConstructor
@Service
public class IOBookService {
    private static final String GET_TITLE_MESSAGE = "Введите название книги:";
    private static final String GET_AUTHOR_ID = "Введите идентификатор автора:";
    private static final String GET_GENRE_ID = "Введите идентификатор жанра:";
    private final IOService ioService;

    public void print(List<Book> books) {
        books.forEach(b -> printBook(b));
    }

    private void printBook(Book book) {
        final var sb = new StringBuffer();
        sb.append("id: " + book.getId());
        sb.append("; ");
        sb.append("title: " + book.getTitle());
        sb.append("; ");
        final var authors = book.getAuthors().stream().map(Author::getName).collect(Collectors.joining(",")).toString();
        sb.append("authors: ");
        sb.append(authors);
        sb.append("; ");
        sb.append("genre: " + book.getGenre().getName());
        ioService.print(sb.toString());
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
