package ru.otus.spring.hw.controllers.reactive;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.repositories.AuthorRepository;

@WebFluxTest({ AuthorRouter.class })
@ComponentScan("ru.otus.spring.hw.controllers.reactive")
public class AuthorRouterFunctionTest {
    @Autowired
    private WebTestClient client;

    @Autowired
    private RouterFunction<?> route;

    @MockBean
    private AuthorRepository authorRepository;

    @Captor
    private ArgumentCaptor<Author> authorCaptor;

    @Test
    void shouldReturnAuthorList(){
        final var author1 = new Author("author1");
        final var author2 = new Author("author2");
        Flux<Author> authorsFlux = Flux.just(author1, author2);

        given(authorRepository.findAll()).willReturn(authorsFlux);

        client.get().uri("/api/authors").accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON).expectBodyList(Author.class)
                .contains(author1, author2);
        then(authorRepository).should().findAll();
    }

    @Test
    void shouldAddNewAuthor() {
        final var authorName = "author name";
        final var savedAuthor = new Author(authorName);
        savedAuthor.setId("132");
        given(authorRepository.save(any())).willReturn(Mono.just(savedAuthor));

        client.post().uri("/api/authors").accept(MediaType.APPLICATION_JSON).bodyValue(new Author(authorName))
                .exchange().expectHeader().contentType(MediaType.APPLICATION_JSON).expectStatus().isCreated();

        then(authorRepository).should().save(authorCaptor.capture());
        assertThat(authorCaptor.getValue().getName()).isEqualTo(authorName);
    }

    @Test
    void shouldNotSaveAuthorWithEmptyName(){
        final var savedAuthor = new Author();

        client.post().uri("/api/authors").accept(MediaType.APPLICATION_JSON).bodyValue(savedAuthor).exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON).expectStatus().isBadRequest().expectBody()
                .jsonPath("$.timestamp").isNotEmpty();

        then(authorRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldRemoveAuthor() {
        final var authorId = "123";
        client.delete().uri("/api/authors/{id}", authorId).accept(MediaType.APPLICATION_JSON).exchange()
            .expectStatus().isOk();
        then(authorRepository).should().deleteById(authorId);
    }

    @Test
    void shouldReturnAuthorById() {
        final var authorName = "author2";
        final var author = new Author(authorName);
        given(authorRepository.findByName(any())).willReturn(Mono.just(author));
        final var client = WebTestClient.bindToRouterFunction(route).build();

        client.get().uri("/func/person?name=" + authorName).exchange().expectStatus().isOk().expectBody(Author.class)
                .isEqualTo(author);

        then(authorRepository).should().findByName(authorName);
    }

    @Test
    void shouldReturnBadRequestStatusIfAuthorNotExist() {
        final var authorName = "author2";
        given(authorRepository.findByName(any())).willReturn(Mono.empty());
        final var client = WebTestClient.bindToRouterFunction(route).build();

        client.get().uri("/func/person?name=" + authorName).exchange().expectStatus().isBadRequest().expectBody()
                .jsonPath("$.timestamp").isNotEmpty().jsonPath("$.errors")
                .value(hasItem("Please provide a author name"));

        then(authorRepository).should().findByName(authorName);
    }
}
