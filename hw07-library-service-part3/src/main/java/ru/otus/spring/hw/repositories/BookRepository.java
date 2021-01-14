package ru.otus.spring.hw.repositories;

import java.util.List;
import java.util.Optional;

import ru.otus.spring.hw.model.Book;

public interface BookRepository {

    List<Book> findAll();

    Optional<Book> findById(long id);

    Book save(Book book);

    void remove(long id);
}
