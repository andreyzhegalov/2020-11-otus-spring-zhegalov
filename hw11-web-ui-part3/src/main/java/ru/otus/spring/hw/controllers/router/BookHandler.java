package ru.otus.spring.hw.controllers.router;

import java.util.List;
import java.util.Objects;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import ru.otus.spring.hw.controllers.dto.BookDto;
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.model.Genre;
import ru.otus.spring.hw.repositories.AuthorRepository;
import ru.otus.spring.hw.repositories.BookRepository;
import ru.otus.spring.hw.repositories.GenreRepository;
import ru.otus.spring.hw.repositories.RepositoryException;

@RequiredArgsConstructor
@Component
public class BookHandler {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final CustomValidator<BookDto> validator;

    private static class Tuple {
        BookDto book;
        Genre genre;
        List<Author> authorList;
    }

    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(bookRepository.findAll().map(BookDto::new), BookDto.class);
    }

    public Mono<ServerResponse> saveBook(ServerRequest request) {
        final Mono<BookDto> newBook = request.bodyToMono(BookDto.class);
        final Mono<BookDto> validBookDto = newBook.doOnNext(validator::validate);

        final var tuple = new Tuple();
        final var savedBookDto = validBookDto.doOnNext(bookDto -> tuple.book = bookDto)
                .flatMap(bookDto -> genreRepository.findById(bookDto.getGenreId())).defaultIfEmpty(new Genre())
                // take genre
                .flatMapIterable(genre -> {
                    if (Objects.isNull(genre.getId())) {
                        throw new RepositoryException("genre not exist ");
                    }
                    tuple.genre = genre;
                    return tuple.book.getAuthorsId();
                })
                // take authors
                .flatMap(authorRepository::findById).defaultIfEmpty(new Author()).doOnNext(author -> {
                    if (Objects.isNull(author.getId())) {
                        throw new RepositoryException("author not exist ");
                    }
                }).buffer(100).last().map(authorList -> {
                    tuple.authorList = authorList;
                    return tuple;
                })
                // make book
                .map(t -> {
                    return new Book(t.book.getId(), t.book.getTitle(), t.genre, t.authorList.toArray(Author[]::new));
                }).flatMap(bookRepository::save).defaultIfEmpty(new Book()).map(BookDto::new).log();

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(savedBookDto, BookDto.class);
    }
}
