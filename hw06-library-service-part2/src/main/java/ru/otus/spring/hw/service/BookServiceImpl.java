package ru.otus.spring.hw.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.dto.BookDto;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.repositories.AuthorRepository;
import ru.otus.spring.hw.repositories.BookRepository;
import ru.otus.spring.hw.repositories.GenreRepository;

@RequiredArgsConstructor
@Transactional
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    @Override
    public void deleteBook(long id) {
        bookRepository.remove(id);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public void save(BookDto bookDto) {
        final var author = authorRepository.findById(bookDto.getAuthorId())
                .orElseThrow(() -> new ServiceException("Author with id " + bookDto.getAuthorId() + " not exist"));
        final var genre = genreRepository.findById(bookDto.getGenreId())
                .orElseThrow(() -> new ServiceException("Genre with id " + bookDto.getGenreId() + " not exist"));
        bookRepository.save(new Book(bookDto.getId(), bookDto.getTitle(), author, genre));
    }

}
