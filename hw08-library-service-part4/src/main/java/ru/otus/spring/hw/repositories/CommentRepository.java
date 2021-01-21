package ru.otus.spring.hw.repositories;

import java.util.List;
import java.util.Optional;

import ru.otus.spring.hw.dto.CommentDto;
import ru.otus.spring.hw.model.Comment;

public interface CommentRepository {

    List<CommentDto> findAllBy();

    Optional<Comment> findById(Long id);

    void save(Comment comment);

    void deleteById(Long id);
}
