package ru.otus.spring.hw.controllers.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BookController {

    @GetMapping({ "/", "/books" })
    public String startPage() {
        return "redirect:/books.html";
    }
}
