package ru.otus.spring.hw.service;

import java.util.List;

import ru.otus.spring.hw.dto.AuthorDto;
import ru.otus.spring.hw.dto.BookDto;

public interface BookService {

    void save(BookDto bookDto);

    void deleteBook(String id);

    List<String> findAll();

    void addAuthor(String bookId, AuthorDto authorDto);

    void removeAuthor(String bookId, AuthorDto authorDto);
}
