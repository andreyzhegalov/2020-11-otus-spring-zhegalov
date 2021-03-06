package ru.otus.spring.hw.controllers.router;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class BookRouter {

    @Bean
    public RouterFunction<ServerResponse> bookComposedRoutes(BookHandler handler) {
        return route().GET("/api/books", handler::findAll).POST("/api/books", handler::saveBook)
                .DELETE("/api/books/{id}", handler::deleteBook).build();
    }
}
