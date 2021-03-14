package ru.otus.spring.hw.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.dao.AuthorRepository;
import ru.otus.spring.hw.dto.BookDb;

@RequiredArgsConstructor
@Service
public class BookService {

    private final AuthorRepository authorRepository;

    public  BookDb addAuthors( BookDb initBook) {
        final var authorList = authorRepository.getByBookId(initBook.getId());
        initBook.setAuthors(authorList);
        return initBook;
    }
}

