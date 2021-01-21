package ru.otus.spring.hw.repositories;

import java.util.List;
import java.util.Optional;

import ru.otus.spring.hw.model.Book;

public interface BookRepository {

    List<Book> findAll();

    Optional<Book> findById(Long id);

    void deleteById(Long id);

    void save(Book book);
}
