package ru.otus.spring.hw.service;

import java.util.List;

import ru.otus.spring.hw.controllers.dto.CommentDto;

public interface CommentService {

    List<CommentDto> findAll();

    List<CommentDto> findAllByBookId(String id);

    CommentDto addComment(CommentDto commentDto);

    boolean deleteById(String id);
}
