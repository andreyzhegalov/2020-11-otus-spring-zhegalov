package ru.otus.spring.hw.service;

import java.util.List;

import ru.otus.spring.hw.dto.CommentDto;

public interface CommentService {

    void addComment(CommentDto commentDto);

    List<CommentDto> findAll();

    List<CommentDto> findAllByBookId(String id);

    void deleteById(String id);
}
