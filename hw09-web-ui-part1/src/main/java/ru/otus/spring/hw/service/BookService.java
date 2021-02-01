package ru.otus.spring.hw.service;

import java.util.List;

import ru.otus.spring.hw.dto.BookDtoInput;
import ru.otus.spring.hw.model.Book;

public interface BookService {

    void save(BookDtoInput bookDto);

    void deleteBook(String id);

    List<Book> findAll();
}
