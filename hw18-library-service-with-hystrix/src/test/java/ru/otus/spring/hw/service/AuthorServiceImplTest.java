package ru.otus.spring.hw.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ru.otus.spring.hw.controllers.dto.AuthorDto;
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.repositories.AuthorRepository;

@SpringBootTest
public class AuthorServiceImplTest {
    @Import(AuthorServiceImpl.class)
    @Configuration
    public static class TestContext {
    }

    @Autowired
    private AuthorService authorService;

    @MockBean
    private AuthorRepository authorRepository;

    @Test
    void shouldReturnAllAuthors() {
        final var expectedAuthorList = List.of(new Author());
        given(authorRepository.findAll()).willReturn(expectedAuthorList);

        final var authorList = authorService.findAll();

        then(authorRepository).should().findAll();
        assertThat(authorList).hasSameElementsAs(expectedAuthorList);
    }

    @Test
    void shouldSaveAuthor() {
        final var savedAuthor = new Author();
        savedAuthor.setId("123");
        given(authorRepository.save(any())).willReturn(savedAuthor);
        authorService.saveAuthor(new AuthorDto());
        then(authorRepository).should().save(any());
    }

    @Test
    void shouldDeleteAuthorById(){
        authorService.deleteAuthor("123");
        then(authorRepository).should().deleteById(anyString());
    }
}
