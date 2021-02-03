package ru.otus.spring.hw.service;

import java.util.List;

import ru.otus.spring.hw.dto.BookDto;
import ru.otus.spring.hw.model.Book;

public interface BookService {

    void save(BookDto bookDto);

    void deleteBook(String id);

    List<Book> findAll();
}
