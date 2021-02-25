package ru.otus.spring.hw.controllers.router;

import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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

    private static class Holder {
        BookDto book;
        Genre genre;
        List<Author> authorList;
    }

    @Transactional(readOnly = true)
    public @NotNull Mono<ServerResponse> findAll(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(bookRepository.findAll().map(BookDto::new), BookDto.class);
    }

    @Transactional
    public @NotNull Mono<ServerResponse> saveBook(ServerRequest request) {
        final Mono<BookDto> newBook = request.bodyToMono(BookDto.class);
        final Mono<BookDto> validBookDto = newBook.doOnNext(validator::validate);

        final var holder = new Holder();
        final Mono<BookDto> savedBookDto = validBookDto.doOnNext(bookDto -> holder.book = bookDto)
                // take genre
                .flatMap(bookDto -> genreRepository.findById(bookDto.getGenreId())).defaultIfEmpty(new Genre())
                .doOnNext(genre -> {
                    if (Objects.isNull(genre.getId())) {
                        throw new RepositoryException("genre not exist ");
                    }
                    holder.genre = genre;
                })
                // take authors
                .flatMapIterable(unused -> holder.book.getAuthorsId()).flatMap(authorRepository::findById).buffer(100)
                .last().map(authorList -> {
                    if (holder.book.getAuthorsId().size() != authorList.size()) {
                        throw new RepositoryException("author not exist ");
                    }
                    holder.authorList = authorList;
                    return holder;
                })
                // make book
                .map(t -> {
                    return new Book(t.book.getId(), t.book.getTitle(), t.genre, t.authorList.toArray(Author[]::new));
                }).flatMap(bookRepository::save).defaultIfEmpty(new Book()).map(BookDto::new);

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(savedBookDto, BookDto.class);
    }

    @Transactional
    public @NotNull Mono<ServerResponse> deleteBook(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN)
                .body(bookRepository.deleteById(request.pathVariable("id")), String.class);
    }
}
