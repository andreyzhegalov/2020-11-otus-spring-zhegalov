package ru.otus.spring.hw.rest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.service.CommentService;

@RequiredArgsConstructor
@Controller
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/comments")
    public String findAll(@RequestParam("bookId") String bookId, Model model) {
        final var bookComments = commentService.findAllByBookId(bookId);
        model.addAttribute("comments", bookComments);
        return "comments";
    }
}
