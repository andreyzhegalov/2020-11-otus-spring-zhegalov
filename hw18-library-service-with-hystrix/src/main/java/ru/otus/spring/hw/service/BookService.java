package ru.otus.spring.hw.service;

import java.util.List;

import ru.otus.spring.hw.controllers.dto.BookDto;
import ru.otus.spring.hw.model.Book;

public interface BookService {

    BookDto save(BookDto bookDto);

    boolean deleteBook(String id);

    List<Book> findAll();
}
