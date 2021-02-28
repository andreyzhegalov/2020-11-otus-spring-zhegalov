package ru.otus.spring.hw.controllers.router;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import ru.otus.spring.hw.controllers.dto.BookDto;
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Genre;
import ru.otus.spring.hw.repositories.AuthorRepository;
import ru.otus.spring.hw.repositories.GenreRepository;

@ActiveProfiles("withMongo")
@SpringBootTest
@AutoConfigureWebTestClient
public class BookRouterIntegratedTest {

    private static final Duration TIMEOUT = Duration.ofMillis(500);
    private Genre existedGenre;
    private List<Author> existedAuthors;

    @Autowired
    private WebTestClient client;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @BeforeEach
    void setUp() {
        existedGenre = genreRepository.findAll().blockFirst(TIMEOUT);
        existedAuthors = authorRepository.findAll().buffer().blockFirst(TIMEOUT);
    }

    @Test
    void shouldAddNewBookForExistedAuthorAndGenre() {
        final var authorsId = existedAuthors.stream().map(Author::getId).collect(Collectors.toList())
                .toArray(new String[0]);
        final var newBookDto = new BookDto();
        newBookDto.setTitle("new book");
        newBookDto.setGenreId(existedGenre.getId());
        newBookDto.setAuthorsId(existedAuthors.stream().map(Author::getId).collect(Collectors.toList()));

        client.post().uri("/api/books").accept(MediaType.APPLICATION_JSON).bodyValue(newBookDto).exchange()
                .expectStatus().isOk().expectBody(BookDto.class).value(bookDto -> {
                    assertThat(bookDto.getId()).isNotNull().isNotBlank();
                    assertThat(bookDto.getGenreId()).isNotNull().isEqualTo(existedGenre.getId());
                    assertThat(bookDto.getAuthorsId()).isNotNull().containsExactlyInAnyOrder(authorsId);
                });
    }
}
