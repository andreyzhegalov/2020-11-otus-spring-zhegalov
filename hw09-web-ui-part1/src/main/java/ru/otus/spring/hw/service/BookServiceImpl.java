package ru.otus.spring.hw.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.dto.BookDtoInput;
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

    private Author getAuthorByName(String authorName) {
        return authorRepository.findByName(authorName)
                .orElseThrow(() -> new ServiceException("Author with name " + authorName + " not exist"));
    }

    private Genre getGenreByName(String genreName) {
        return genreRepository.findByName(genreName)
                .orElseThrow(() -> new ServiceException("Genre with name " + genreName + " not exist"));
    }

    @Override
    public void save(BookDtoInput bookDto) {
        final var genre = getGenreByName(bookDto.getGenreName());
        final var book = new Book(bookDto.getId(), bookDto.getTitle(), genre,
                bookDto.getAuthorsName().stream().map(this::getAuthorByName).toArray(Author[]::new));
        bookRepository.save(book);
    }
}
