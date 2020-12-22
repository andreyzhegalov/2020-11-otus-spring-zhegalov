package ru.otus.spring.hw.service;

import java.util.List;

import ru.otus.spring.hw.model.Book;

public interface IOBookService {

    void printBooks(List<Book> books);

    Book getBook();
}

