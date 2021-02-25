package ru.otus.spring.hw.controllers.router;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class CommentRouter {

    @Bean
    public RouterFunction<ServerResponse> commentsComposedRoutes(CommentHandler handler) {
        return route()
            .GET("/api/comments", handler::findAll)
            .POST("/api/comments", handler::saveComment)
            .DELETE("/api/comments/{id}", handler::deleteComment)
            .build();
    }
}

