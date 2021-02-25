package ru.otus.spring.hw.controllers.router;

import static org.mockito.ArgumentMatchers.any;
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
import reactor.core.publisher.Mono;
import ru.otus.spring.hw.controllers.dto.CommentDto;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.model.Comment;
import ru.otus.spring.hw.repositories.BookRepository;
import ru.otus.spring.hw.repositories.CommentRepository;

@WebFluxTest({ CommentRouter.class })
@Import({ CommentHandler.class, GlobalErrorAttributes.class, CustomValidator.class })
public class CommentRouterTest {
    @Autowired
    private WebTestClient client;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private BookRepository bookRepository;

    @Test
    void shouldReturnBadRequestIfBookIdNotSet() {
        client.get().uri(uriBuilder -> uriBuilder.path("/api/comments").queryParam("notBookId", "123").build())
                .accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isBadRequest();
        then(commentRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldReturnAllComments() {
        final var bookId = "123";
        final var commentDto = new CommentDto("comment1", bookId);
        given(commentRepository.findAllDtoByBookId(bookId)).willReturn(Flux.just(commentDto));

        client.get().uri(uriBuilder -> uriBuilder.path("/api/comments").queryParam("bookId", bookId).build())
                .accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk().expectHeader()
                .contentType(MediaType.APPLICATION_JSON).expectBodyList(CommentDto.class).contains(commentDto);

        then(commentRepository).should().findAllDtoByBookId(bookId);
    }

    @Test
    void shouldSaveNewComment() {
        final var bookId = "123";
        final var commentDto = new CommentDto("Comment1", bookId);
        final var commentBook = new Book();
        commentBook.setId("321");
        final var savedComment = new Comment("commentId", "comment1", commentBook);

        given(bookRepository.findById(bookId)).willReturn(Mono.just(commentBook));
        given(commentRepository.save(any())).willReturn(Mono.just(savedComment));

        client.post().uri("/api/comments").accept(MediaType.APPLICATION_JSON).bodyValue(commentDto).exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON).expectStatus().isOk();

        then(bookRepository).should().findById(bookId);
        then(commentRepository).should().save(any());
    }

    @Test
    void shouldNotSaveNewCommentIfTheBookNotExist() {
        final var bookId = "123";
        final var commentDto = new CommentDto("Comment1", bookId);

        given(bookRepository.findById(bookId)).willReturn(Mono.empty());

        client.post().uri("/api/comments").accept(MediaType.APPLICATION_JSON).bodyValue(commentDto).exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON).expectStatus().isBadRequest();

        then(bookRepository).should().findById(bookId);
        then(commentRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldNotSaveCommentWithoutText() {
        final var bookId = "123";
        final var commentDto = new CommentDto("", bookId);

        given(bookRepository.findById(bookId)).willReturn(Mono.empty());

        client.post().uri("/api/comments").accept(MediaType.APPLICATION_JSON).bodyValue(commentDto).exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON).expectStatus().isBadRequest().expectBody()
                .jsonPath("$.errors").isNotEmpty();

        then(bookRepository).shouldHaveNoInteractions();
        then(commentRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldDeleteComment() {
        final var commentId = "123";
        given(commentRepository.deleteById(commentId)).willReturn(Mono.empty());
        client.delete().uri("/api/comments/{id}", commentId).accept(MediaType.APPLICATION_JSON).exchange()
                .expectStatus().isOk();
        then(commentRepository).should().deleteById(commentId);
    }
}
