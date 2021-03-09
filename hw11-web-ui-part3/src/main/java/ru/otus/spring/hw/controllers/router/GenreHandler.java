package ru.otus.spring.hw.controllers.router;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import ru.otus.spring.hw.controllers.dto.GenreDto;
import ru.otus.spring.hw.repositories.BookRepository;
import ru.otus.spring.hw.repositories.GenreRepository;

@RequiredArgsConstructor
@Component
public class GenreHandler {
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final CustomValidator<GenreDto> validator;

    @Transactional(readOnly = true)
    public @NotNull Mono<ServerResponse> findAll(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(genreRepository.findAll().map(GenreDto::new), GenreDto.class);
    }

    @Transactional
    public @NotNull Mono<ServerResponse> saveGenre(ServerRequest request) {
        Mono<GenreDto> newGenre = request.bodyToMono(GenreDto.class);
        Mono<GenreDto> savedGenreDto = newGenre.doOnNext(validator::validate).map(GenreDto::toEntity)
                .flatMap(genreRepository::save).map(GenreDto::new);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(savedGenreDto, GenreDto.class);
    }

    @Transactional
    public @NotNull Mono<ServerResponse> deleteGenre(ServerRequest request) {
        final var handler = bookRepository.existsBookByGenre_id(request.pathVariable("id")).flatMap(genreHasBook -> {
            if (genreHasBook) {
                throw new CustomRouterException("genre can't deleted with existed book");
            }
            final var genreId = request.pathVariable("id");
            return genreRepository.deleteById(genreId);
        });
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).body(handler, String.class);
    }
}
