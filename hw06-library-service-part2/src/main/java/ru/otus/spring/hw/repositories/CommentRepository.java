package ru.otus.spring.hw.repositories;

import java.util.List;
import java.util.Optional;

import ru.otus.spring.hw.model.Comment;

public interface CommentRepository {

    List<Comment> findAll();

    Optional<Comment> findById(long id);

    Comment save(Comment genre);

    void remove(long id);

    void deleteByBookId(long bookId);
}
