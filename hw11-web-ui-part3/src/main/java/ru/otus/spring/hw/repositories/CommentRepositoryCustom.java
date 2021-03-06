package ru.otus.spring.hw.repositories;

import reactor.core.publisher.Flux;
import ru.otus.spring.hw.controllers.dto.CommentDto;

public interface CommentRepositoryCustom {

    Flux<CommentDto> findAllDto();

    Flux<CommentDto> findAllDtoByBookId(String bookId);

}
