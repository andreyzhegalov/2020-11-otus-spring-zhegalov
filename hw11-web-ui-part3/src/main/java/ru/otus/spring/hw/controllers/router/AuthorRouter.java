package ru.otus.spring.hw.controllers.router;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class AuthorRouter {

    @Bean
    public RouterFunction<ServerResponse> authorComposedRoutes(AuthorHandler handler) {
        return route().GET("/api/authors", handler::findAll).POST("/api/authors", handler::saveAuthor)
                .DELETE("/api/authors/{id}", handler::deleteAuthor).build();
    }

}
