package ru.otus.spring.hw.repositories;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.dto.CommentDto;
import ru.otus.spring.hw.model.Comment;

@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<CommentDto> findAllDto() {
        final var aggregation = Aggregation.newAggregation(project("id", "text").and("$book").as("bookId"));
        return mongoTemplate.aggregate(aggregation, Comment.class, CommentDto.class).getMappedResults();
    }
}
