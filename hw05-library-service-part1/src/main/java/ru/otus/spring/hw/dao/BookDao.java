package ru.otus.spring.hw.dao;

import java.util.List;

import ru.otus.spring.hw.model.Book;

public interface BookDao {

    long insertOrUpdate(Book book);

    List<Book> getAll();

}
