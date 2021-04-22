package ru.otus.spring.hw.controllers.rest;

import java.util.Collections;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.controllers.dto.CommentDto;
import ru.otus.spring.hw.service.CommentService;

@RequiredArgsConstructor
@RestController
public class CommentRestController {
    private final CommentService commentService;

    @GetMapping("/api/comments")
    @HystrixCommand(fallbackMethod = "getAllCommentsFallbackHandler")
    List<CommentDto> getAllComments(@RequestParam("bookId") @NotBlank String bookId) {
        return commentService.findAllByBookId(bookId);
    }

    @SuppressWarnings("unused")
    private List<CommentDto> getAllCommentsFallbackHandler(String bookId) {
        return Collections.emptyList();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/comments")
    @HystrixCommand(fallbackMethod = "saveCommentFallbackHandler")
    CommentDto saveComment(@Valid @RequestBody CommentDto comment) {
        return commentService.addComment(comment);
    }

    @SuppressWarnings("unused")
    private CommentDto saveCommentFallbackHandler(CommentDto comment) {
        return new CommentDto();
    }

    @DeleteMapping("/api/comments/{id}")
    @HystrixCommand(fallbackMethod = "deleteCommentFallbackHandler")
    void deleteComment(@PathVariable("id") @NotBlank String id) {
        commentService.deleteById(id);
    }

    @SuppressWarnings("unused")
    private void deleteCommentFallbackHandler(String id) {
    }
}
