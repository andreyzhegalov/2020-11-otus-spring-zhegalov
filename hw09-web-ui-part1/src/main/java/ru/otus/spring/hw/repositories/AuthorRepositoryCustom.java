package ru.otus.spring.hw.repositories;

import java.util.List;

import ru.otus.spring.hw.dto.AuthorDto;

public interface AuthorRepositoryCustom {

    void removeBookArrayElementsById(String id);

    List<AuthorDto> findAllDto();
}
