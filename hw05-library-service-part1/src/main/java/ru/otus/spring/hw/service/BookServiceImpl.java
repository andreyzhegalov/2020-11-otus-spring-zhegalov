package ru.otus.spring.hw.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.dao.AuthorDao;
import ru.otus.spring.hw.dao.BookDao;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.model.Converters;
import ru.otus.spring.hw.model.dto.BookDto;

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
        if (mayBeBook.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(getBook(mayBeBook.get()));
    }

    @Override
    public List<Book> getAllBooks() {
        return bookDao.getAll().stream().map(b -> getBook(b)).collect(Collectors.toList());

    }

    private Book getBook(BookDto bookDto) {
        final var author = authorDao.getById(bookDto.getAuthorId());
        if (author.isEmpty()) {
            throw new ServiceException(String.format("Author with id = %d not existed", bookDto.getAuthorId()));
        }
        return Converters.convert(bookDto, author.get());
    }

}
