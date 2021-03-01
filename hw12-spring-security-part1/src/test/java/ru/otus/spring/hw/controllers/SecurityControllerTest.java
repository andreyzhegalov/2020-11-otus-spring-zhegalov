package ru.otus.spring.hw.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.imageio.plugins.tiff.ExifGPSTagSet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import ru.otus.spring.hw.repositories.AuthorRepository;
import ru.otus.spring.hw.repositories.GenreRepository;
import ru.otus.spring.hw.service.BookService;


@WebMvcTest(controllers = BookController.class)
public class SecurityControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @Test
    void shouldRedirectToLoginForAllRequest() throws Exception {
        mvc.perform(get("/")).andDo(print()).andExpect(status().is(302))
            .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void shouldNotLoginForNotExistedUser() throws Exception{
        mvc.perform(formLogin().user("admin").password("password"))
            .andDo(print())
            .andExpect(status().isFound())
            .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    void shouldLoginSuccessful() throws Exception{
        mvc.perform(formLogin().user("user").password("password"))
            .andDo(print())
            .andExpect(status().isFound())
            .andExpect(redirectedUrl("/"));
    }

    @Test
    void shouldReturnOkForAuthenticatedUser() throws Exception {
        mvc.perform(get("/books")
                .with(user("admin").password("pass").roles("ADMIN"))
                )
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    void shouldRedirectToLoginForAnonymous() throws Exception{
        mvc.perform(get("/").with(anonymous())).andDo(print()).andExpect(status().is(302))
            .andExpect(redirectedUrlPattern("**/login"));
    }

}

