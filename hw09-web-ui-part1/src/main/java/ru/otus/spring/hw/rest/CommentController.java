package ru.otus.spring.hw.rest;

import javax.validation.constraints.NotBlank;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.dto.CommentDto;
import ru.otus.spring.hw.service.CommentService;

@RequiredArgsConstructor
@Controller
@Validated
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/comments")
    public String getAll(@RequestParam("bookId") @NotBlank String bookId, Model model) {
        final var bookComments = commentService.findAllByBookId(bookId);
        model.addAttribute("comments", bookComments);
        model.addAttribute("bookId", bookId);
        return "comments";
    }

    @PostMapping("/comments")
    public String saveComment(@RequestParam("bookId") @NotBlank String bookId, @NotBlank String text,
            RedirectAttributes redirectAttributes) {
        commentService.addComment(new CommentDto(text, bookId));
        redirectAttributes.addAttribute("bookId", bookId);
        return "redirect:/comments";
    }

    @DeleteMapping("/comments")
    public String removeById(@RequestParam("id") @NotBlank String id, @NotBlank String bookId,
            RedirectAttributes redirectAttributes) {
        commentService.deleteById(id);
        redirectAttributes.addAttribute("bookId", bookId);
        return "redirect:/comments";
    }
}
