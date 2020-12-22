package ru.otus.spring.hw.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.dao.AuthorDao;
import ru.otus.spring.hw.dao.BookDao;
import ru.otus.spring.hw.dao.GenreDao;
import ru.otus.spring.hw.dao.dto.BookDto;
import ru.otus.spring.hw.model.Book;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;

    @Override
    public void saveBook(Book book) {
        final var authorId = authorDao.insertOrUpdate(book.getAuthor());
        final var genreId = genreDao.insertOrUpdate(book.getGenre());
        final var bookDto = new BookDto(book.getId(), book.getTitle(), authorId, genreId);
        bookDao.insertOrUpdate(bookDto);
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
        return bookDao.getAll().stream().map(this::getBook).collect(Collectors.toList());

    }

    private Book getBook(BookDto bookDto) {
        final var author = authorDao.getById(bookDto.getAuthorId());
        if (author.isEmpty()) {
            throw new ServiceException(String.format("Author with id = %d not existed", bookDto.getAuthorId()));
        }
        final var genre = genreDao.getById(bookDto.getGenreId());
        if (genre.isEmpty()) {
            throw new ServiceException(String.format("Genre with id = %d not existed", bookDto.getGenreId()));
        }
        return new Book(bookDto.getId(), bookDto.getTitle(), author.get(), genre.get());
    }

}
