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

    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(bookRepository.findAll().map(BookDto::new), BookDto.class);
    }

    public Mono<ServerResponse> saveBook(ServerRequest request) {
        Mono<BookDto> newBook = request.bodyToMono(BookDto.class);
        final var validBookDto = newBook.doOnNext(validator::validate);

        final var monoGenre = newBook.flatMap(bookDto -> genreRepository.findById(bookDto.getGenreId())).log()
                .defaultIfEmpty(new Genre()).doOnNext(genre -> {
                    if (Objects.isNull(genre.getId())) {
                        throw new RepositoryException("genre not exist ");
                    }
                });

        final Mono<List<Author>> monoAuthorList = validBookDto.flatMapIterable(BookDto::getAuthorsId)
                .flatMap(authorId -> {
                    return authorRepository.findById(authorId).defaultIfEmpty(new Author()).doOnNext(author -> {
                        if (Objects.isNull(author.getId())) {
                            throw new RepositoryException("author not exist ");
                        }
                    });
                }).buffer(100).last();

        final var savedBookDto = Mono.zip(validBookDto, monoGenre, monoAuthorList).log().map(tuple -> {
            final var bookDto = tuple.getT1();
            final var genre = tuple.getT2();
            final var authors = tuple.getT3();
            return new Book(bookDto.getId(), bookDto.getTitle(), genre, authors.toArray(Author[]::new));
        }).flatMap(bookRepository::save).map(BookDto::new);

        return ServerResponse.created(null).contentType(MediaType.APPLICATION_JSON).body(savedBookDto, BookDto.class);
    }
}
