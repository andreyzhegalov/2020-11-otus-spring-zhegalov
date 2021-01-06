package ru.otus.spring.hw.service;

import java.util.List;

import ru.otus.spring.hw.dto.CommentDto;
import ru.otus.spring.hw.model.Comment;

public interface CommentService {

    void addComment(CommentDto commentDto);

    List<Comment> findAll();

    void deleteByBookId(long bookId);
}
