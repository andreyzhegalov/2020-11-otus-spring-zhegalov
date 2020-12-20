package ru.otus.spring.hw.dao;

import java.util.List;
import java.util.Optional;

import ru.otus.spring.hw.model.Book;

public interface BookDao {

    Optional<Book> getById(long id);

    List<Book> getAll();

    long insertBook(Book book);

    void updateBook(Book book);

    void insertOrUpdate(Book book);

    void deleteBook(long id);

}
