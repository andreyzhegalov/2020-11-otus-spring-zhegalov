package ru.otus.spring.hw.controllers.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import reactor.core.publisher.Mono;

@Controller
public class BookController {

    @GetMapping({ "/", "/books" })
    public Mono<String> startPage() {
        return Mono.just("redirect:/books.html");
    }
}
