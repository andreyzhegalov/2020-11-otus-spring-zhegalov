package ru.otus.spring.hw.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ru.otus.spring.hw.dto.AuthorDto;
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

    @Captor
    private ArgumentCaptor<Author> captor;

    @Test
    void shouldSaveAuthorDto() {
        final var authorDto = new AuthorDto();
        authorDto.setId("123");
        authorDto.setName("author name");

        authorService.saveAuthorDto(authorDto);
        then(authorRepository).should().save(captor.capture());

        assertThat(captor.getValue().getId()).isEqualTo(authorDto.getId());
        assertThat(captor.getValue().getName()).isEqualTo(authorDto.getName());
    }

    @Test
    void shouldReturnAllAuthors() {
        authorService.findAllDto();
        then(authorRepository).should().findAll();
    }

    @Test
    void shouldDeleteById() {
        final var deletedId = "123";
        authorService.deleteById(deletedId);
        then(authorRepository).should().deleteById(eq(deletedId));
    }
}
