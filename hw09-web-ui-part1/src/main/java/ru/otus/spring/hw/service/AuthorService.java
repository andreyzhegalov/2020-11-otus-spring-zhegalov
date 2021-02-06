package ru.otus.spring.hw.service;

import java.util.List;

import ru.otus.spring.hw.dto.AuthorDto;

public interface AuthorService {

    void saveAuthorDto(AuthorDto authorDto);

    List<AuthorDto> findAllDto();

    void deleteById(String id);
}
