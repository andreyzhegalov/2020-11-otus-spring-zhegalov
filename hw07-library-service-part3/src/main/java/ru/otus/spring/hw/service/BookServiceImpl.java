package ru.otus.spring.hw.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.dto.AuthorDto;
import ru.otus.spring.hw.dto.BookDto;
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.model.Genre;
import ru.otus.spring.hw.repositories.AuthorRepository;
import ru.otus.spring.hw.repositories.BookRepository;
import ru.otus.spring.hw.repositories.GenreRepository;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    @Transactional
    @Override
    public void deleteBook(long id) {
        bookRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAllBy(BookDto.class);
    }

    @Transactional
    @Override
    public void save(BookDto bookDto) {
        final var genre = getGenreById(bookDto.getGenreId());
        final var book = new Book(bookDto.getId(), bookDto.getTitle(), genre);

        for (final var id : bookDto.getAuthorIds()) {
            book.addAuthor(getAuthorById(id));
        }

        bookRepository.save(book);
    }

    @Transactional
    @Override
    public void addAuthor(long bookId, AuthorDto authorDto) {
        final var book = getBookById(bookId);
        final var author = getAuthorById(authorDto.getId());
        book.addAuthor(author);
        bookRepository.save(book);
    }

    @Transactional
    @Override
    public void removeAuthor(long bookId, AuthorDto authorDto) {
        final var book = getBookById(bookId);
        final var author = getAuthorById(authorDto.getId());
        book.removeAuthor(author);
        bookRepository.save(book);
    }

    private Author getAuthorById(long authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new ServiceException("Author with id " + authorId + " not exist"));
    }

    private Genre getGenreById(long genreId) {
        return genreRepository.findById(genreId)
                .orElseThrow(() -> new ServiceException("Genre with id " + genreId + " not exist"));
    }

    private Book getBookById(long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new ServiceException("Book with id " + bookId + " not found"));
    }
}
