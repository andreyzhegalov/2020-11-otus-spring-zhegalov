package ru.otus.spring.hw.security;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import ru.otus.spring.hw.repositories.AuthorRepository;
import ru.otus.spring.hw.repositories.GenreRepository;
import ru.otus.spring.hw.repositories.UserRepository;
import ru.otus.spring.hw.service.BookService;
import ru.otus.spring.hw.service.CommentService;

@WebMvcTest
@ComponentScan({ "ru.otus.spring.hw.security" })
public class SecurityControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CommentService commentService;

    @Test
    void shouldRedirectToLoginPageForAllRequest() throws Exception {
        mvc.perform(get("/")).andDo(print()).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void shouldRedirectToLoginForAnonymous() throws Exception {
        mvc.perform(get("/").with(anonymous())).andDo(print()).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser
    void shouldReturnOkForAuthenticatedUser() throws Exception {
        mvc.perform(get("/books")).andDo(print()).andExpect(status().isOk());
    }

}
