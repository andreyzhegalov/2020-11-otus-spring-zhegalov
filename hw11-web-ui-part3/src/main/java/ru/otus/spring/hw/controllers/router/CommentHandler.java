package ru.otus.spring.hw.controllers.router;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import ru.otus.spring.hw.controllers.dto.CommentDto;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.model.Comment;
import ru.otus.spring.hw.repositories.BookRepository;
import ru.otus.spring.hw.repositories.CommentRepository;

@RequiredArgsConstructor
@Component
public class CommentHandler {

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;
    private final CustomValidator<CommentDto> validator;

    public @NotNull Mono<ServerResponse> findAll(ServerRequest request) {
        final var bookId = request.queryParam("bookId")
                .orElseThrow(() -> new CustomRouterException("bookId not defined"));

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(commentRepository.findAllDtoByBookId(bookId), CommentDto.class);
    }

    @NoArgsConstructor
    private final static class Holder {
        CommentDto commentDto;
    }

    public @NotNull Mono<ServerResponse> saveComment(ServerRequest request) {
        final var holder = new Holder();
        final Mono<CommentDto> commentDtoMono = request.bodyToMono(CommentDto.class);
        final Mono<CommentDto> handler = commentDtoMono.doOnNext(validator::validate)
                .doOnNext(dto -> holder.commentDto = dto).flatMap(dto -> bookRepository.findById(dto.getBookId()))
                .defaultIfEmpty(new Book()).doOnNext(book -> {
                    if (Objects.isNull(book.getId())) {
                        throw new CustomRouterException(
                                "book with id " + holder.commentDto.getBookId() + " not  exist");
                    }
                }).map(book -> {
                    final var comment = new Comment();
                    comment.setText(holder.commentDto.getText());
                    comment.setBook(book);
                    return comment;
                }).flatMap(commentRepository::save).map(CommentDto::new);

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(handler, CommentDto.class);
    }

    public @NotNull Mono<ServerResponse> deleteComment(ServerRequest request) {
        final var handler = Mono.just(request.pathVariable("id")).log().flatMap(commentRepository::deleteById);
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).body(handler, String.class);
    }
}
