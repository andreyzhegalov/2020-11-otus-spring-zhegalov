package ru.otus.spring.hw.controllers.router;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class BookRouter {

    // @GetMapping("/api/books")
    // public List<BookDto> findAll() {
    //     return bookService.findAll().stream().map(BookDto::new).collect(Collectors.toList());
    // }
    //
    // @PostMapping("/api/books")
    // @ResponseStatus(HttpStatus.CREATED)
    // public BookDto saveAuthor(@Valid @RequestBody BookDto bookDto) {
    //     return bookService.save(bookDto);
    // }
    //
    // @DeleteMapping("/api/books/{id}")
    // public void deleteBook(@PathVariable("id") @NotBlank String id) {
    //     bookService.deleteBook(id);
    // }

    @Bean
    public RouterFunction<ServerResponse> bookComposedRoutes(BookHandler handler) {
        return route()
            .GET("/api/books", handler::findAll)
            // .POST("/api/authors", handler::saveAuthor)
            // .DELETE("/api/authors/{id}", handler::deleteAuthor)
            .build();
    }
}

