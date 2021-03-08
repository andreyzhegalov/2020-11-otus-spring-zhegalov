package ru.otus.spring.hw.security;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import ru.otus.spring.hw.repositories.AuthorRepository;
import ru.otus.spring.hw.repositories.GenreRepository;
import ru.otus.spring.hw.service.BookService;
import ru.otus.spring.hw.service.CommentService;

@WebMvcTest
public class MethodAuthorizationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private CommentService commentService;

    @Test
    @WithMockUser(roles = { "EDITOR" })
    void onlyUserWithEditorRoleCanAddAuthor() throws Exception {
        mvc.perform(post("/books").param("title", "book title").param("genreId", "genreId")
                .param("authorsId", "authorId").param("authorsId", "authorsId")).andDo(print())
                .andExpect(status().isFound()).andExpect(view().name("redirect:/books"));

        then(bookService).should().save(any());
    }

    @Test
    @WithMockUser(roles = { "ADMIN" })
    void userWithAdminRoleCanNotAddAuthor() throws Exception {
        mvc.perform(post("/books").param("title", "book title").param("genreId", "genreId")
                .param("authorsId", "authorId").param("authorsId", "authorsId")).andDo(print())
                .andExpect(status().isForbidden());

        then(bookService).shouldHaveNoInteractions();
    }

}
