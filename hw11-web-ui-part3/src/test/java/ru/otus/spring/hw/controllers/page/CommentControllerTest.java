package ru.otus.spring.hw.controllers.page;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import ru.otus.spring.hw.controllers.router.GlobalErrorAttributes;

@WebFluxTest({ CommentController.class })
@Import(GlobalErrorAttributes.class)
public class CommentControllerTest {
    @Autowired
    private WebTestClient client;

    @Test
    void shouldAddBookIdToModel() {
        client.get().uri("/comments?bookId=123").accept(MediaType.ALL).exchange().expectStatus().isOk();
    }
}
