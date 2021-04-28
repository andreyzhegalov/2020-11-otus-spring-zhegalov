package ru.otus.spring.hw.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.internal.stubbing.answers.AnswersWithDelay;
import org.mockito.internal.stubbing.answers.Returns;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ru.otus.spring.hw.controllers.dto.AuthorDto;
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.repositories.AuthorRepository;

@SpringBootTest
public class AuthorServiceImplTest {
    private final static int CIRCUIT_BREAKER_TIMEOUT = 500;

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
    void shouldReturnEmptyListIfRepositoryNotResponse() throws InterruptedException {
        final var authorList = new Returns(List.of(new Author()));
        final var answersWithDelay = new AnswersWithDelay(2 * CIRCUIT_BREAKER_TIMEOUT, authorList);
        doAnswer(answersWithDelay).when(authorRepository).findAll();

        final var authorListFromCircuitBreaker = authorService.findAll();

        then(authorRepository).should().findAll();
        assertThat(authorListFromCircuitBreaker).isEmpty();
    }

    @Test
    void shouldSaveAuthor(){
        authorService.saveAuthor(new AuthorDto());
        then(authorRepository).should().save(any());
    }

    @Test
    void shouldReturnEmptyAuthorWhenSaveIfRepositryNotResponse(){
        final var actualSavedAuthor = new Author();
        actualSavedAuthor.setId("123");

        final var authorReturn = new Returns(actualSavedAuthor);
        final var answersWithDelay = new AnswersWithDelay(2 * CIRCUIT_BREAKER_TIMEOUT, authorReturn);
        doAnswer(answersWithDelay).when(authorRepository).save(any());

        final var savedAuthorFromCircuitBreaker = authorService.saveAuthor(new AuthorDto( new Author()));

        then(authorRepository).should().save(any());
        assertThat(savedAuthorFromCircuitBreaker).isNotNull();
        assertThat(savedAuthorFromCircuitBreaker.getId()).isNull();
    }

}
