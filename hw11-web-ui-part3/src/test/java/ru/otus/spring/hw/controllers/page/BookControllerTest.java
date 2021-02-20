package ru.otus.spring.hw.controllers.page;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import ru.otus.spring.hw.controllers.router.GlobalErrorAttributes;

@WebFluxTest
@Import(GlobalErrorAttributes.class)
public class BookControllerTest {

    @Autowired
    private BookController bookController;

    @Test
    void shouldRedirectFromRootToBooks() {
        // final var client = WebTestClient.bindToController(bookController).build();
        // client.get().uri("/").accept(MediaType.TEXT_HTML).exchange().expectStatus().isOk();
        //
        // // mvc.perform(get("/")).andExpect(view().name("redirect:/books.html"));
    }
}
