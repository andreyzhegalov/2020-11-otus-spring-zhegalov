package ru.otus.spring.hw.repositories;

import reactor.core.publisher.Mono;

public interface AuthorRepositoryCustom {

    Mono<Void> removeBookArrayElementsById(String id);
}
