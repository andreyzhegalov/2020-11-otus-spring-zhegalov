package ru.otus.spring.hw.controllers.router;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import ru.otus.spring.hw.controllers.dto.GenreDto;
import ru.otus.spring.hw.repositories.GenreRepository;

@RequiredArgsConstructor
@Component
public class GenreHandler {
    private final GenreRepository genreRepository;
    private final CustomValidator<GenreDto> validator;

    public @NotNull Mono<ServerResponse> findAll(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(genreRepository.findAll().map(GenreDto::new), GenreDto.class);
    }

    public @NotNull Mono<ServerResponse> saveGenre(ServerRequest request) {
        Mono<GenreDto> newGenre = request.bodyToMono(GenreDto.class);
        Mono<GenreDto> savedGenreDto = newGenre.doOnNext(validator::validate).map(GenreDto::toEntity)
                .flatMap(genreRepository::save).map(GenreDto::new);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(savedGenreDto, GenreDto.class);
    }

    public @NotNull Mono<ServerResponse> deleteGenre(ServerRequest request) {
        final Mono<String> id = Mono.just(request.pathVariable("id"));
        final var result = id.doOnNext(genreRepository::deleteById);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(result, Void.class);
    }
}
