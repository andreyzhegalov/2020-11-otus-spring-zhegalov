package ru.otus.spring.hw.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.dao.AuthorDao;
import ru.otus.spring.hw.dao.BookDao;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.model.Converters;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookDao bookDao;
    private final AuthorDao authorDao;

    @Override
    public void saveBook(Book book) {
        authorDao.insertOrUpdate(book.getAuthor());
        bookDao.insertOrUpdate(Converters.convert(book));
    }

    @Override
    public Optional<Book> getBook(long id) {
        final var mayBeBook = bookDao.getById(id);
        if( mayBeBook.isEmpty()) {
            return Optional.empty();
        }
        final var author = authorDao.getById(mayBeBook.get().getAuthorId());
        if( author.isEmpty()){
            throw new ServiceException(String.format("Author with id = %d not existed", id));
        }
        return Optional.of(Converters.convert(mayBeBook.get(), author.get()));
    }

}
