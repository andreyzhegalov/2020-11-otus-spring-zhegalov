package ru.otus.spring.hw.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import ru.otus.spring.hw.model.Author;

@RequiredArgsConstructor
public class AuthorRepositoryCustomImpl implements AuthorRepositoryCustom {

    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<Void> removeBookArrayElementsById(String id) {
        final var query = Query.query(Criteria.where("_id").is(new ObjectId(id)));
        final var update = new Update().pull("books", query);
        return mongoTemplate.updateMulti(new Query(), update, Author.class).then();
    }
}
