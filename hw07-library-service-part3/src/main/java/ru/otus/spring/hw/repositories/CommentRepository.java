package ru.otus.spring.hw.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;

import ru.otus.spring.hw.model.Comment;

public interface CommentRepository extends Repository<Comment, Long> {

    @EntityGraph(attributePaths = { "book" })
    <T> List<T> findAllBy(Class<T> type);

    @EntityGraph(attributePaths = { "book" })
    Optional<Comment> findById(Long id);

    void save(Comment comment);

    void deleteById(Long id);
}
