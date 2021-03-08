package ru.otus.spring.hw.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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

@WebMvcTest()
@ComponentScan({ "ru.otus.spring.hw.security" })
public class UrlAuthorizationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private BookService bookService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private GenreRepository genreRepository;

    @Test
    @WithMockUser(roles = { "ADMIN" })
    public void shouldEnabledAccessToRootUriForAdmin() throws Exception {
        this.mvc.perform(get("/")).andDo(print()).andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(roles = { "USER" })
    public void shouldEnabledAccessToRootUriForUser() throws Exception {
        this.mvc.perform(get("/")).andDo(print()).andExpect(status().is3xxRedirection());
    }

    @ParameterizedTest
    @ValueSource(strings = { "/genres", "/books", "/authors", "/comments?bookId=1" })
    @WithMockUser(roles = { "ADMIN" })
    public void shouldEnabledAccessToAllUriForAdmin(String uri) throws Exception {
        this.mvc.perform(get(uri)).andDo(print()).andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = { "/genres", "/authors" })
    @WithMockUser(roles = { "USER" })
    public void shouldNotEnabledAccessToSomeUriForNotAdminUser(String uri) throws Exception {
        this.mvc.perform(get(uri)).andDo(print()).andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @ValueSource(strings = { "/books", "/comments?bookId=1" })
    @WithMockUser(roles = { "USER" })
    public void shouldEnabledAccessToBooksAndCommentsUriForNotAdminUser(String uri) throws Exception {
        this.mvc.perform(get(uri)).andDo(print()).andExpect(status().isOk());
    }
}
