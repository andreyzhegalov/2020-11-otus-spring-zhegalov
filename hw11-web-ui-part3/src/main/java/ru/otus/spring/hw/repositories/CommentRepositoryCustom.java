package ru.otus.spring.hw.repositories;

import java.util.List;

import ru.otus.spring.hw.controllers.dto.CommentDto;

public interface CommentRepositoryCustom {

    List<CommentDto> findAllDto();

    List<CommentDto> findAllDtoByBookId(String bookId);

}
