package ru.otus.spring.hw.controllers.reactive;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import ru.otus.spring.hw.controllers.dto.AuthorDto;
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.repositories.AuthorRepository;

@RequiredArgsConstructor
@Component
class AuthorHandler {
    private final AuthorRepository authorRepository;
    private final Validator validator;

    public Mono<ServerResponse> getAllAuthors(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(authorRepository.findAll(),
                Author.class);
    }

    private void validate(AuthorDto author) {
        final var errors = new BeanPropertyBindingResult(author, AuthorDto.class.getName());
        validator.validate(author, errors);

        String errorMessages = errors.getFieldErrors().stream().map(x -> x.getDefaultMessage())
                .collect(Collectors.joining(","));

        if (Objects.nonNull(errors) && !errors.getAllErrors().isEmpty()) {
            throw new CustomRouterException(HttpStatus.BAD_REQUEST, errorMessages);
        }
    }

    public Mono<ServerResponse> saveAuthor(ServerRequest request) {
        Mono<AuthorDto> newAuthor = request.bodyToMono(AuthorDto.class);
        Mono<AuthorDto> savedAuthorDto = newAuthor.doOnNext(this::validate).map(AuthorDto::toEntity)
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
