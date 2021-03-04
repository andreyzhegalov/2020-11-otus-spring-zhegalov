package ru.otus.spring.hw.controllers.router;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

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

    @Transactional(readOnly = true)
    public @NotNull Mono<ServerResponse> findAll(ServerRequest request) {
        final var bookId = request.queryParam("bookId")
                .orElseThrow(() -> new CustomRouterException("bookId not defined"));

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(commentRepository.findAllDtoByBookId(bookId), CommentDto.class);
    }

    @Transactional
    public @NotNull Mono<ServerResponse> saveComment(ServerRequest request) {
        final Mono<CommentDto> commentDtoMono = request.bodyToMono(CommentDto.class);
        final Mono<CommentDto> validCommentDto = commentDtoMono.doOnNext(validator::validate);

        final Mono<Comment> monoComment = validCommentDto.flatMap(dto -> {
            return Mono.just(dto).zipWith(bookRepository.findById(dto.getBookId()).switchIfEmpty(Mono.error(() -> {
                throw new CustomRouterException("book with id " + dto.getBookId() + " not  exist");
            })))
            .map(tuple -> {
                final var commentDto = tuple.getT1();
                final Book book = tuple.getT2();
                final var comment = new Comment();
                comment.setText(commentDto.getText());
                comment.setBook(book);
                return comment;
            }).flatMap(commentRepository::save);
        });

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(monoComment.map(CommentDto::new),
                CommentDto.class);
    }

    @Transactional
    public @NotNull Mono<ServerResponse> deleteComment(ServerRequest request) {
        final var handler = Mono.just(request.pathVariable("id")).flatMap(commentRepository::deleteById);
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).body(handler, String.class);
    }
}
