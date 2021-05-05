package ru.otus.spring.hw.service;

import java.util.Collections;
import java.util.List;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

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
    @HystrixCommand(fallbackMethod = "deleteBookFallbackHandler")
    @Override
    public boolean deleteBook(String id) {
        bookRepository.deleteById(id);
        return true;
    }

    @SuppressWarnings("unused")
    private boolean deleteBookFallbackHandler(String id) {
        return false;
    }

    @Transactional(readOnly = true)
    @HystrixCommand(fallbackMethod = "findAllFallbackHandler")
    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @SuppressWarnings("unused")
    private List<Book> findAllFallbackHandler() {
        return Collections.emptyList();
    }

    private Author getAuthorById(String authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new ServiceException("Author with id " + authorId + " not exist"));
    }

    @Transactional
    @HystrixCommand(fallbackMethod = "saveAuthorFallbackHandler")
    @Override
    public BookDto save(BookDto bookDto) {
        final var genre = genreRepository.findById(bookDto.getGenreId())
                .orElseThrow(() -> new ServiceException("Genre with id " + bookDto.getGenreId() + " not exist"));
        final var book = new Book(bookDto.getId(), bookDto.getTitle(), genre,
                bookDto.getAuthorsId().stream().map(this::getAuthorById).toArray(Author[]::new));
        return new BookDto(bookRepository.save(book));
    }

    @SuppressWarnings("unused")
    private BookDto saveAuthorFallbackHandler(BookDto bookDto) {
        return new BookDto();
    }
}
