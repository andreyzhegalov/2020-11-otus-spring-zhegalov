package ru.otus.spring.hw.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.dto.AuthorDto;
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.repositories.AuthorRepository;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    public void saveAuthorDto(AuthorDto authorDto) {
        authorRepository.save(toEntity(authorDto));
    }

    private Author toEntity(AuthorDto dto) {
        final var author = new Author(dto.getName());
        author.setId(dto.getId());
        return author;
    }

    @Override
    public List<AuthorDto> findAllDto() {
        return authorRepository.findAll().stream().map(AuthorDto::new).collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        authorRepository.deleteById(id);
    }
}
