package ru.otus.spring.hw.dao;

import ru.otus.spring.hw.model.Book;

public interface BookDao {

    long insertOrUpdate(Book book);

}

