package ru.otus.spring.hw.service;

import java.util.List;

import ru.otus.spring.hw.dto.AuthorDto;
import ru.otus.spring.hw.dto.BookDto;

public interface BookService {

    void save(BookDto bookDto);

    void deleteBook(long id);

    List<BookDto> findAll();

    void addAuthor(long bookId, AuthorDto authorDto);

    void removeAuthor(long bookId, AuthorDto authorDto);
}
