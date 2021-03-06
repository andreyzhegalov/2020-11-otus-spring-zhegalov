package ru.otus.spring.hw.controllers.router;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.List;

import com.google.common.collect.ImmutableList;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
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
import ru.otus.spring.hw.controllers.dto.BookDto;
import ru.otus.spring.hw.model.Author;
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

    @Captor
    private ArgumentCaptor<Book> captor;

    private String genreId;
    private Genre genre;
    private String author1Id;
    private Author author1;
    private String author2Id;
    private Author author2;
    private List<Author> authorList;
    private Book savedBook;

    @BeforeEach
    void setUp() {
        genreId = "genreId";
        genre = new Genre("genre1");
        genre.setId(genreId);

        author1Id = new String("authorId1");
        author1 = new Author("name1");
        author1.setId(author1Id);
        author2Id = new String("authorId2");
        author2 = new Author("name2");
        author2.setId(author2Id);
        authorList = ImmutableList.of(author1, author2);

        savedBook = new Book();
        savedBook.setTitle("new book title");
        savedBook.setId("123");
        savedBook.setGenre(genre);
        savedBook.setAuthors(authorList);
    }

    @Test
    void shouldReturnBooksList() {
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
    void shouldNotSaveEmptyBook() {
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

    @Test
    void shouldAddNewBookForExistedAuthorAndGenre() {
        given(genreRepository.findById(genreId)).willReturn(Mono.just(genre));
        given(bookRepository.save(any())).willReturn(Mono.just(savedBook));
        given(authorRepository.findById(author1Id)).willReturn(Mono.just(author1));
        given(authorRepository.findById(author2Id)).willReturn(Mono.just(author2));

        client.post().uri("/api/books").accept(MediaType.APPLICATION_JSON).bodyValue(new BookDto(savedBook)).exchange()
                .expectStatus().isOk().expectBody(BookDto.class)
                .value(bookDto -> assertThat(bookDto.getId()).isNotNull().isNotBlank());

        then(genreRepository).should().findById(genreId);
        then(authorRepository).should().findById(author1Id);
        then(authorRepository).should().findById(author2Id);
        then(bookRepository).should().save(captor.capture());
        assertThat(captor.getValue().getGenre()).isEqualTo(genre);
        assertThat(captor.getValue().getAuthors()).containsExactlyInAnyOrderElementsOf(authorList);
    }

    @Test
    void shouldReturnBadRequestWhenGenreNotExistForTheSavedBook() {
        given(genreRepository.findById(genreId)).willReturn(Mono.empty());
        given(authorRepository.findById(author1Id)).willReturn(Mono.just(author1));
        given(authorRepository.findById(author2Id)).willReturn(Mono.just(author2));

        client.post().uri("/api/books").accept(MediaType.APPLICATION_JSON).bodyValue(new BookDto(savedBook)).exchange()
                .expectStatus().isBadRequest();

        then(bookRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldReturnBadRequestWhenAuthorNotExistForTheSavedBook() {
        given(genreRepository.findById(savedBook.getGenre().getId())).willReturn(Mono.just(savedBook.getGenre()));
        given(authorRepository.findById(author1Id)).willReturn(Mono.just(author1));
        given(authorRepository.findById(author2Id)).willReturn(Mono.empty());

        client.post().uri("/api/books").accept(MediaType.APPLICATION_JSON).bodyValue(new BookDto(savedBook)).exchange()
                .expectStatus().isBadRequest();

        then(bookRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldRemoveBook() {
        final var bookId = "123";
        given(bookRepository.deleteById(bookId)).willReturn(Mono.empty());

        client.delete().uri("/api/books/{id}", bookId).accept(MediaType.APPLICATION_JSON).exchange().expectStatus()
                .isOk();

        then(bookRepository).should().deleteById(bookId);
    }
}
