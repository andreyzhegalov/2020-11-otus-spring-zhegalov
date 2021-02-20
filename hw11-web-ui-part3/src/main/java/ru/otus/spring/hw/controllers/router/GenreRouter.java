package ru.otus.spring.hw.controllers.router;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class GenreRouter {

    @Bean
    public RouterFunction<ServerResponse> genreComposedRoutes(GenreHandler handler) {
        return route()
            .GET("/api/genres", handler::findAll)
            .POST("/api/genres", handler::saveGenre)
            .DELETE("/api/genres/{id}", handler::deleteGenre)
            .build();
    }

}

