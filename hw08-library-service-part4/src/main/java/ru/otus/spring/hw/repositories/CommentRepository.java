package ru.otus.spring.hw.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ru.otus.spring.hw.dto.CommentDto;
import ru.otus.spring.hw.model.Comment;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {

    List<CommentDto> findAllBy();

    List<Comment> findAllByBook_id(String bookId);

    void removeAllByBook_id(String bookId);
}
