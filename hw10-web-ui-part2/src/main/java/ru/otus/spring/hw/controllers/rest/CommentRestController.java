package ru.otus.spring.hw.controllers.rest;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

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
    List<CommentDto> getAllComments(@RequestParam("bookId") @NotBlank String bookId) {
        return commentService.findAllByBookId(bookId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/comments")
    CommentDto saveComment(@Valid @RequestBody CommentDto comment) {
        return commentService.addComment(comment);
    }

    @DeleteMapping("/api/comments/{id}")
    void deleteComment(@PathVariable("id") @NotBlank String id) {
        commentService.deleteById(id);
    }
}
