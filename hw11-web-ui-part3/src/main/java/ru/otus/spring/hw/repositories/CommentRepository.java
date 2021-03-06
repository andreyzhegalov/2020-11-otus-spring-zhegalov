package ru.otus.spring.hw.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.hw.model.Comment;

@Repository
public interface CommentRepository extends ReactiveMongoRepository<Comment, String>, CommentRepositoryCustom {

    Flux<Comment> findAllByBook_id(String bookId);

    Mono<Void> removeAllByBook_id(String bookId);
}
