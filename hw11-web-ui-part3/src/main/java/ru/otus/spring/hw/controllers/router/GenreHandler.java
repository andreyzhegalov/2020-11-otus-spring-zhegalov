package ru.otus.spring.hw.controllers.router;

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
import ru.otus.spring.hw.controllers.dto.GenreDto;
import ru.otus.spring.hw.repositories.GenreRepository;

@RequiredArgsConstructor
@Component
public class GenreHandler {
    // @GetMapping("/api/genres")
    // @Transactional(readOnly = true)
    // public List<GenreDto> findAll() {
    //     return genreRepository.findAll().stream().map(GenreDto::new).collect(Collectors.toList());
    // }
    //
    // @PostMapping("/api/genres")
    // @ResponseStatus(HttpStatus.CREATED)
    // @Transactional
    // public GenreDto saveGenre(@Valid @RequestBody GenreDto genreDto) {
    //     final var savedGenre = genreRepository.save(genreDto.toEntity());
    //     return new GenreDto(savedGenre);
    // }
    //
    // @DeleteMapping("/api/genres/{id}")
    // @Transactional
    // public void deleteGenre(@PathVariable("id") @NotBlank String id) {
    //     genreRepository.deleteById(id);
    // }
    private final GenreRepository genreRepository;

    private final Validator validator;

    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
            .body(genreRepository.findAll().map(GenreDto::new), GenreDto.class);
    }

    private void validate(GenreDto genre) {
        final var errors = new BeanPropertyBindingResult(genre, GenreDto.class.getName());
        validator.validate(genre, errors);

        String errorMessages = errors.getFieldErrors().stream().map(x -> x.getDefaultMessage())
                .collect(Collectors.joining(","));

        if (Objects.nonNull(errors) && !errors.getAllErrors().isEmpty()) {
            throw new CustomRouterException(HttpStatus.BAD_REQUEST, errorMessages);
        }
    }

    public Mono<ServerResponse> saveGenre(ServerRequest request) {
        Mono<GenreDto> newGenre = request.bodyToMono(GenreDto.class);
        Mono<GenreDto> savedGenreDto = newGenre.doOnNext(this::validate).map(GenreDto::toEntity)
                .flatMap(genreRepository::save).map(GenreDto::new);
        return ServerResponse.created(null).contentType(MediaType.APPLICATION_JSON).body(savedGenreDto,
                GenreDto.class);
    }

    public Mono<ServerResponse> deleteGenre(ServerRequest request) {
        final Mono<String> id = Mono.just(request.pathVariable("id"));
        final var result = id.doOnNext(genreRepository::deleteById).onErrorMap(e -> {
            throw new CustomRouterException(HttpStatus.BAD_REQUEST, e.getMessage());
        });
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(result, Void.class);
    }
}

