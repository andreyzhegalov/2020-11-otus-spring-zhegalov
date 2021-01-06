package ru.otus.spring.hw.service;

import java.util.List;

import ru.otus.spring.hw.dto.AuthorDto;
import ru.otus.spring.hw.dto.BookDto;
import ru.otus.spring.hw.model.Book;

public interface BookService {

    void save(BookDto bookDto);

    void deleteBook(long id);

    List<Book> findAll();

    void addAuthor(long bookId, AuthorDto authorDto);

    void removeAuthor(long bookId, AuthorDto authorDto);
}
