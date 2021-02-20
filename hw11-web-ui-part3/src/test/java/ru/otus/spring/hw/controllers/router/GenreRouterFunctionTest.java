package ru.otus.spring.hw.controllers.router;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.hw.controllers.dto.GenreDto;
import ru.otus.spring.hw.model.Genre;
import ru.otus.spring.hw.repositories.GenreRepository;
import ru.otus.spring.hw.repositories.RepositoryException;

@WebFluxTest({ GenreRouter.class })
@Import({ GenreHandler.class, GlobalErrorAttributes.class })
public class GenreRouterFunctionTest {
    @Autowired
    private WebTestClient client;

    @MockBean
    private GenreRepository genreRepository;

    @Captor
    private ArgumentCaptor<Genre> genreCaptor;

    @Test
    void shouldReturnGenreList() {
        final var genre1 = new Genre("genre1");
        final var genre2 = new Genre("genre2");
        Flux<Genre> genreFlux = Flux.just(genre1, genre2);

        given(genreRepository.findAll()).willReturn(genreFlux);

        client.get().uri("/api/genres").accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON).expectBodyList(Genre.class)
                .contains(genre1, genre2);
        then(genreRepository).should().findAll();
    }

    @Test
    void shouldAddNewGenre() {
        final var genreName = "genre name";
        final var savedGenre = new Genre(genreName);
        savedGenre.setId("132");
        given(genreRepository.save(any())).willReturn(Mono.just(savedGenre));

        client.post().uri("/api/genres").accept(MediaType.APPLICATION_JSON).bodyValue(new GenreDto(savedGenre))
                .exchange().expectHeader().contentType(MediaType.APPLICATION_JSON).expectStatus().isCreated();

        then(genreRepository).should().save(genreCaptor.capture());
        assertThat(genreCaptor.getValue().getName()).isEqualTo(genreName);
    }

    @Test
    void shouldNotSaveGenreWithEmptyName() {

        final var savedGenre = new GenreDto();

        client.post().uri("/api/genres").accept(MediaType.APPLICATION_JSON).bodyValue(savedGenre).exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON).expectStatus().isBadRequest().expectBody()
                .jsonPath("$.timestamp").isNotEmpty().jsonPath("$.errors").isEqualTo("Please provide a genre name");

        then(genreRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldRemoveGenre() {
        final var genreId = "123";
        client.delete().uri("/api/genres/{id}", genreId).accept(MediaType.APPLICATION_JSON).exchange()
            .expectStatus().isOk();
        then(genreRepository).should().deleteById(genreId);
    }

    @Test
    void shouldReturnErrorIfDeletedGenreHasBook() {
        final var genreId = "id_genre_with_book";
        final var errorMessage = "error";
        doThrow(new RepositoryException(errorMessage)).when(genreRepository).deleteById(genreId);

        client.delete().uri("/api/genres/{id}", genreId).accept(MediaType.APPLICATION_JSON).exchange().expectStatus()
                .isBadRequest().expectBody().jsonPath("$.errors").isEqualTo(errorMessage);

        then(genreRepository).should().deleteById(genreId);
    }
}
