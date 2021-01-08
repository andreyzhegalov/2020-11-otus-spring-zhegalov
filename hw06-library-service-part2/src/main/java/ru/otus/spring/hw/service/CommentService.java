package ru.otus.spring.hw.service;

import java.util.List;

import ru.otus.spring.hw.dto.CommentDto;

public interface CommentService {

    void addComment(CommentDto commentDto);

    List<CommentDto> findAll();

    void deleteByBookId(long bookId);
}
