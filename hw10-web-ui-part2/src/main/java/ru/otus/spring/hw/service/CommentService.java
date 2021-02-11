package ru.otus.spring.hw.service;

import java.util.List;

import ru.otus.spring.hw.controllers.dto.CommentDto;

public interface CommentService {

    CommentDto addComment(CommentDto commentDto);

    List<CommentDto> findAll();

    List<CommentDto> findAllByBookId(String id);

    void deleteById(String id);
}
