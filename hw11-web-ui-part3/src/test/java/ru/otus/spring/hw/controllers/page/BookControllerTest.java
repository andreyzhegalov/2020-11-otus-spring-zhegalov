package ru.otus.spring.hw.controllers.page;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import ru.otus.spring.hw.controllers.router.GlobalErrorAttributes;

@WebFluxTest({ BookController.class })
@Import(GlobalErrorAttributes.class)
public class BookControllerTest {
    @Autowired
    private WebTestClient client;

    @Test
    void shouldRedirectFromRootToBooks() {
        client.get().uri("/").accept(MediaType.ALL).exchange().expectStatus().isSeeOther().expectHeader()
                .location("/books.html");
    }
}
