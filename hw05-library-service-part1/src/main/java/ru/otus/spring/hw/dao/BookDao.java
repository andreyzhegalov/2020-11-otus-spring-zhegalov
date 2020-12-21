package ru.otus.spring.hw.dao;

import java.util.List;
import java.util.Optional;

import ru.otus.spring.hw.model.dto.BookDto;


public interface BookDao {

    Optional<BookDto> getById(long id);

    List<BookDto> getAll();

    long insertBook(BookDto book);

    void updateBook(BookDto book);

    void insertOrUpdate(BookDto book);

    void deleteBook(long id);

}
