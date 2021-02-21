package ru.otus.spring.hw.controllers.router;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;

import org.hamcrest.Matchers;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;
import ru.otus.spring.hw.controllers.dto.BookDto;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.model.Genre;
import ru.otus.spring.hw.repositories.AuthorRepository;
import ru.otus.spring.hw.repositories.BookRepository;
import ru.otus.spring.hw.repositories.GenreRepository;

@WebFluxTest({ BookRouter.class })
@Import({ GlobalErrorAttributes.class, BookHandler.class, CustomValidator.class })
public class BookRouterTest {
    @Autowired
    private WebTestClient client;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @Test
    void shouldReturnBooksList() {
        final var genre = new Genre();
        genre.setId("genreId");
        genre.setName("genreName");

        final var book1 = new Book();
        book1.setId("1");
        book1.setTitle("book1");
        book1.setGenre(genre);
        final var book2 = new Book();
        book2.setId("2");
        book2.setTitle("book2");
        book2.setGenre(genre);

        given(bookRepository.findAll()).willReturn(Flux.just(book1, book2));

        client.get().uri("/api/books").accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON).expectBodyList(BookDto.class)
                .contains(new BookDto(book1), new BookDto(book2));

        then(bookRepository).should().findAll();
    }

    @Test
    void shouldNotSaveBookWithOutTitle() throws Exception {
        final var bookDto = new BookDto();
        bookDto.setTitle(" ");
        assertThat(bookDto.getTitle()).isBlank();
        final var body = client.post().uri("/api/books").accept(MediaType.APPLICATION_JSON).bodyValue(bookDto)
                .exchange().expectHeader().contentType(MediaType.APPLICATION_JSON).expectStatus().isBadRequest()
                .expectBody().jsonPath("$.timestamp").isNotEmpty().jsonPath("$.errors");
        body.value(Matchers.containsString("provide a book title"));
        body.value(Matchers.containsString("provide a genre"));
        body.value(Matchers.containsString("provide a authors"));

        then(bookRepository).shouldHaveNoInteractions();
    }
}
