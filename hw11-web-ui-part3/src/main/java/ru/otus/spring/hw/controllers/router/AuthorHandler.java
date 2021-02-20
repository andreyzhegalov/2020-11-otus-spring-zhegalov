package ru.otus.spring.hw.controllers.router;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import ru.otus.spring.hw.controllers.dto.AuthorDto;
import ru.otus.spring.hw.repositories.AuthorRepository;

@RequiredArgsConstructor
@Component
class AuthorHandler {
    private final AuthorRepository authorRepository;
    private final CustomValidator<AuthorDto> validator;

    public Mono<ServerResponse> getAllAuthors(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(authorRepository.findAll().map(AuthorDto::new), AuthorDto.class);
    }

    public Mono<ServerResponse> saveAuthor(ServerRequest request) {
        Mono<AuthorDto> newAuthor = request.bodyToMono(AuthorDto.class);
        Mono<AuthorDto> savedAuthorDto = newAuthor.doOnNext(validator::validate).map(AuthorDto::toEntity)
                .flatMap(authorRepository::save).map(AuthorDto::new);
        return ServerResponse.created(null).contentType(MediaType.APPLICATION_JSON).body(savedAuthorDto,
                AuthorDto.class);
    }

    public Mono<ServerResponse> deleteAuthor(ServerRequest request) {
        final Mono<String> id = Mono.just(request.pathVariable("id"));
        final var result = id.doOnNext(authorRepository::deleteById).onErrorMap(e -> {
            throw new CustomRouterException(HttpStatus.BAD_REQUEST, e.getMessage());
        });
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(result, Void.class);
    }
}
