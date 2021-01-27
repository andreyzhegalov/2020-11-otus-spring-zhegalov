package ru.otus.spring.hw.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
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

    @Override
    public void deleteBook(String id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public void save(BookDto bookDto) {
        final var genre = getGenreById(bookDto.getGenreId());
        final var book = new Book(bookDto.getId(), bookDto.getTitle(), genre,
                bookDto.getAuthorIds().stream().map(this::getAuthorById).toArray(Author[]::new));
        bookRepository.save(book);
    }

    private Author getAuthorById(String authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new ServiceException("Author with id " + authorId + " not exist"));
    }

    private Genre getGenreById(String genreId) {
        return genreRepository.findById(genreId)
                .orElseThrow(() -> new ServiceException("Genre with id " + genreId + " not exist"));
    }
}
