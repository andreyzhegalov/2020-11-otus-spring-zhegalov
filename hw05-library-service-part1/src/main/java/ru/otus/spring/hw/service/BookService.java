package ru.otus.spring.hw.service;

import java.util.List;
import java.util.Optional;

import ru.otus.spring.hw.model.Book;

public interface BookService {

    void saveBook(Book book);

    Optional<Book> getBook(long id);

    List<Book> getAllBooks();

    void deleteBook(long id);

    void updateBook(Book book);

}
