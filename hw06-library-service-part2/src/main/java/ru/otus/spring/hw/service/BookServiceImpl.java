package ru.otus.spring.hw.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.dto.AuthorDto;
import ru.otus.spring.hw.dto.BookDto;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.model.Comment;
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
        bookRepository.remove(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Transactional
    @Override
    public void save(BookDto bookDto) {
        final var author = authorRepository.findById(bookDto.getAuthorId())
                .orElseThrow(() -> new ServiceException("Author with id " + bookDto.getAuthorId() + " not exist"));
        final var genre = genreRepository.findById(bookDto.getGenreId())
                .orElseThrow(() -> new ServiceException("Genre with id " + bookDto.getGenreId() + " not exist"));
        bookRepository.save(new Book(bookDto.getId(), bookDto.getTitle(), author, genre));
    }

    @Transactional
    @Override
    public void addComment(long bookId, Comment comment) {
        final var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ServiceException("Book with id " + bookId + " not found"));

        book.addComment(comment);
        bookRepository.save(book);
    }

    @Transactional
    @Override
    public void addAuthor(long bookId, AuthorDto authorDto) {
        final var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ServiceException("Book with id " + bookId + " not found"));
        final var author = authorRepository.findById(authorDto.getId())
                .orElseThrow(() -> new ServiceException("Author with id " + authorDto.getId() + " not exist"));
        book.addAuthor(author);
        bookRepository.save(book);
    }

    @Transactional
    @Override
    public void removeAuthor(long bookId, AuthorDto authorDto) {
        final var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ServiceException("Book with id " + bookId + " not found"));
        final var author = authorRepository.findById(authorDto.getId())
                .orElseThrow(() -> new ServiceException("Author with id " + authorDto.getId() + " not exist"));
        book.removeAuthor(author);
        bookRepository.save(book);
    }
}
