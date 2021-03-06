package ru.otus.spring.hw.controllers.router;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
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

    @Transactional(readOnly = true)
    public @NotNull Mono<ServerResponse> findAll(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(bookRepository.findAll().map(BookDto::new), BookDto.class);
    }

    @Transactional
    public @NotNull Mono<ServerResponse> saveBook(ServerRequest request) {
        final Mono<BookDto> newBook = request.bodyToMono(BookDto.class);
        final Mono<BookDto> validBookDto = newBook.doOnNext(validator::validate);

        final Mono<Book> book = validBookDto.flatMap(dto -> {
            final Mono<Genre> monoGenre = genreRepository.findById(dto.getGenreId()).switchIfEmpty(Mono.error(() -> {
                throw new RepositoryException("genre not exist ");
            }));

            final Mono<List<Author>> monoAuthorList = Flux.fromIterable(dto.getAuthorsId()).flatMap(authorId -> {
                return authorRepository.findById(authorId).switchIfEmpty(Mono.error(() -> {
                    throw new RepositoryException("author not exist ");
                }));
            }).collectList();

            return Mono.zip(monoGenre, monoAuthorList).map(tuple -> {
                final var genre = tuple.getT1();
                final var authors = tuple.getT2();
                return new Book(dto.getId(), dto.getTitle(), genre, authors.toArray(Author[]::new));
            }).flatMap(bookRepository::save);
        });

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(book.map(BookDto::new), BookDto.class);

    }

    @Transactional
    public @NotNull Mono<ServerResponse> deleteBook(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN)
                .body(bookRepository.deleteById(request.pathVariable("id")), String.class);
    }
}
