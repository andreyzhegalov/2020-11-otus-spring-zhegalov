package ru.otus.spring.hw.controllers.router;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import ru.otus.spring.hw.controllers.dto.BookDto;
import ru.otus.spring.hw.repositories.BookRepository;

@RequiredArgsConstructor
@Component
public class BookHandler {
    private final BookRepository bookRepository;
    private final CustomValidator validator;

    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(bookRepository.findAll().map(book -> {
                    return new BookDto(book);
                }), BookDto.class);
    }
}
