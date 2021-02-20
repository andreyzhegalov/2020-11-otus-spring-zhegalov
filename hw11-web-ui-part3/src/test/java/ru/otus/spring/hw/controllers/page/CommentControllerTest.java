package ru.otus.spring.hw.controllers.page;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;

import ru.otus.spring.hw.controllers.router.GlobalErrorAttributes;


@WebFluxTest
@Import(GlobalErrorAttributes.class)
public class CommentControllerTest {


    @Test
    void shouldAddBookIdToModel() {
        // final var bookId = "123";
        // mvc.perform(get("/comments/").param("bookId", bookId)).andDo(print())
        //         .andExpect(model().attributeExists("bookId")).andExpect(model().attribute("bookId", is(bookId)))
        //         .andExpect(view().name("comments"));
    }
}
