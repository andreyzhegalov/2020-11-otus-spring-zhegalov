package ru.otus.spring.hw.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.controllers.dto.AuthorDto;
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.repositories.AuthorRepository;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    @Transactional
    public AuthorDto saveAuthor(AuthorDto authorDto) {
        final var savedAuthor = authorRepository.save(authorDto.toEntity());
        return new AuthorDto(savedAuthor);
    }

    @Override
    @Transactional
    public void deleteAuthor(String id) {
        authorRepository.deleteById(id);
    }

}
