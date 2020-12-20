package ru.otus.spring.hw.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.dao.BookDao;
import ru.otus.spring.hw.model.Book;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService{
    private final BookDao bookDao;

    @Override
    public long saveBook(Book book) {
        return bookDao.insertOrUpdate(book);
    }

}
