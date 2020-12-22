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
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.model.Genre;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;

    @Override
    public void saveBook(Book book) {
        final var authorId = book.getAuthor().getId();
        final var author = getAuthor(authorId);
        final var genreId = book.getGenre().getId();
        final var genre = getGenre(genreId);
        final var bookDto = new BookDto(book.getId(), book.getTitle(), author.getId(), genre.getId());
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

    private Author getAuthor(long id) {
        final var author = authorDao.getById(id);
        if (author.isEmpty()) {
            throw new ServiceException(String.format("Author with id = %d not existed", id));
        }
        return author.get();
    }

    private Genre getGenre(long id) {
        final var genre = genreDao.getById(id);
        if (genre.isEmpty()) {
            throw new ServiceException(String.format("Genre with id = %d not existed", id));
        }
        return genre.get();
    }

    private Book getBook(BookDto bookDto) {
        final var author = getAuthor(bookDto.getAuthorId());
        final var genre = getGenre(bookDto.getGenreId());
        return new Book(bookDto.getId(), bookDto.getTitle(), author, genre);
    }

}
