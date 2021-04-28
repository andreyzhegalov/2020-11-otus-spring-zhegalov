package ru.otus.spring.hw.service;

import java.util.Collections;
import java.util.List;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

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
    @HystrixCommand(fallbackMethod = "getAllAuthorsFallbackHandler")
    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @SuppressWarnings("unused")
    private List<Author> getAllAuthorsFallbackHandler() {
        return Collections.emptyList();
    }

    @Override
    @Transactional
    @HystrixCommand(fallbackMethod = "saveAuthorFallbackHandler")
    public AuthorDto saveAuthor(AuthorDto authorDto) {
        final var savedAuthor = authorRepository.save(authorDto.toEntity());
        return new AuthorDto(savedAuthor);
    }

    @SuppressWarnings("unused")
    private AuthorDto saveAuthorFallbackHandler(AuthorDto authorDto) {
        return new AuthorDto();
    }

    @Override
    @Transactional
    @HystrixCommand(fallbackMethod = "deleteAuthorFallbackHandler" )
    public void deleteAuthor(String id) {
        authorRepository.deleteById(id);
    }

    @SuppressWarnings("unused")
    private void deleteAuthorFallbackHandler(String id){
    }
}
