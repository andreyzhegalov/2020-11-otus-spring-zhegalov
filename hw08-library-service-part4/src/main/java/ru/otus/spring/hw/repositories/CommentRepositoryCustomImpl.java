package ru.otus.spring.hw.repositories;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.lookup;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.aggregation.ObjectOperators.ObjectToArray.valueOfToArray;

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
        final var aggregation = Aggregation.newAggregation(
                project("id", "text").andExclude("_id").and(valueOfToArray("book")).as("book_map"),
                project("id", "text").and("book_map").arrayElementAt(1).as("book_id_map"),
                project("id", "text").and("book_id_map.v").as("book_id"), lookup("books", "book_id", "_id", "book"),
                unwind("book"), project("id", "text").and("book_id").as("bookId").and("book.title").as("bookTitle"));
        return mongoTemplate.aggregate(aggregation, Comment.class, CommentDto.class).getMappedResults();
    }
}
