package ru.otus.spring.hw.controllers.page;

import javax.validation.constraints.NotBlank;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@Validated
public class CommentController {
    @GetMapping("/comments")
    public String getAll(@RequestParam("bookId") @NotBlank String bookId, Model model) {
        model.addAttribute("bookId", bookId);
        return "comments";
    }
}
