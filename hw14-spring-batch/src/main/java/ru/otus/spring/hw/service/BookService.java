package ru.otus.spring.hw.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.dao.AuthorRepository;
import ru.otus.spring.hw.model.BookDb;
import ru.otus.spring.hw.model.BookMongo;

@RequiredArgsConstructor
@Service
public class BookService {

    private final AuthorRepository authorRepository;

    public  BookDb addAuthors( BookDb initBook) {
        final var authorList = authorRepository.getByBookId(initBook.getId());
        initBook.setAuthors(authorList);
        return initBook;
    }

    public BookDb setObjectId(BookDb initBook) {
        return initBook;
    }


    public BookMongo convertId(BookDb initBook) {
        return new BookMongo(initBook);
    }
}

