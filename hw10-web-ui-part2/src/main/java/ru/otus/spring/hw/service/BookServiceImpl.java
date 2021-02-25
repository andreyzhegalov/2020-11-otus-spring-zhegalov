package ru.otus.spring.hw.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.controllers.dto.BookDto;
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;
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
    public void deleteBook(String id) {
        bookRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    private Author getAuthorById(String authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new ServiceException("Author with id " + authorId + " not exist"));
    }

    @Transactional
    @Override
    public BookDto save(BookDto bookDto) {
        final var genre = genreRepository.findById(bookDto.getGenreId())
                .orElseThrow(() -> new ServiceException("Genre with id " + bookDto.getGenreId() + " not exist"));
        final var book = new Book(bookDto.getId(), bookDto.getTitle(), genre,
                bookDto.getAuthorsId().stream().map(this::getAuthorById).toArray(Author[]::new));
        return new BookDto(bookRepository.save(book));
    }
}
