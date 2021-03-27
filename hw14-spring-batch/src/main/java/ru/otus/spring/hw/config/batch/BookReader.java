package ru.otus.spring.hw.config.batch;

import java.util.Iterator;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import ru.otus.spring.hw.dao.AuthorRepositoryJdbc;
import ru.otus.spring.hw.dao.BookRepositoryJdbc;
import ru.otus.spring.hw.model.Book;

@StepScope
@Component
public class BookReader implements ItemReader<Book<Long>> {
    private final Iterator<Book<Long>> iterator;
    private final AuthorRepositoryJdbc authorRepository;

    public BookReader(BookRepositoryJdbc bookRepository, AuthorRepositoryJdbc authorRepository) {
        this.authorRepository = authorRepository;
        this.iterator = bookRepository.findAll().iterator();
    }

    @Override
    public Book<Long> read() throws Exception {
        if (!iterator.hasNext()) {
            return null;
        }
        final var book = iterator.next();
        final var authorList = authorRepository.getByBookId(book.getId());
        book.setAuthors(authorList);
        return book;
    }

}
