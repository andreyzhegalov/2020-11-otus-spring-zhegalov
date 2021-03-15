package ru.otus.spring.hw.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.dao.AuthorRepositoryJdbc;
import ru.otus.spring.hw.model.Book;

@RequiredArgsConstructor
@Service
public class BookService {

    private final AuthorRepositoryJdbc authorRepository;

    public Book<Long> addAuthorsToBook(Book<Long> initBook) {
        final var authorList = authorRepository.getByBookId(initBook.getId());
        initBook.setAuthors(authorList);
        return initBook;
    }
}
