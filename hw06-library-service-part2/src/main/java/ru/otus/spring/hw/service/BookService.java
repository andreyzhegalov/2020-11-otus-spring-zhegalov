package ru.otus.spring.hw.service;

import java.util.List;

import ru.otus.spring.hw.dto.BookDto;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.model.Comment;

public interface BookService {

    void save(BookDto bookDto);

    void deleteBook(long id);

    List<Book> findAll();

    void addComment(long bookId, Comment comment);
}
