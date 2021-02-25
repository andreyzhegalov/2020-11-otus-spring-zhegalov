package ru.otus.spring.hw.controllers.router;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import ru.otus.spring.hw.controllers.dto.AuthorDto;
import ru.otus.spring.hw.repositories.AuthorRepository;
import ru.otus.spring.hw.repositories.BookRepository;

@RequiredArgsConstructor
@Component
class AuthorHandler {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final CustomValidator<AuthorDto> validator;

    public @NotNull Mono<ServerResponse> findAll(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(authorRepository.findAll().map(AuthorDto::new), AuthorDto.class);
    }

    public @NotNull Mono<ServerResponse> saveAuthor(ServerRequest request) {
        Mono<AuthorDto> newAuthor = request.bodyToMono(AuthorDto.class);
        Mono<AuthorDto> savedAuthorDto = newAuthor.doOnNext(validator::validate).map(AuthorDto::toEntity)
                .flatMap(authorRepository::save).map(AuthorDto::new);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(savedAuthorDto, AuthorDto.class);
    }

    public @NotNull Mono<ServerResponse> deleteAuthor(ServerRequest request) {
        final var handler = Mono.just(request.pathVariable("id")).log().flatMap(bookRepository::existsBookByAuthors_id)
                .doOnNext(authorHasBook -> {
                    if (authorHasBook) {
                        throw new CustomRouterException("author can't deleted with existed book");
                    }
                }).flatMap(unused -> {
                    final var authorId = request.pathVariable("id");
                    return authorRepository.deleteById(authorId);
                });
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).body(handler, String.class);
    }
}
