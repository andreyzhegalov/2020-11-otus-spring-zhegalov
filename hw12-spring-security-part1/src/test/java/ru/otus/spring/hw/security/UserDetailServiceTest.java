package ru.otus.spring.hw.security;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import ru.otus.spring.hw.controllers.AuthorController;
import ru.otus.spring.hw.repositories.AuthorRepository;
import ru.otus.spring.hw.repositories.UserRepository;

@WebMvcTest(controllers = AuthorController.class)
@ComponentScan({ "ru.otus.spring.hw.security" })
public class UserDetailServiceTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private PasswordEncoder encoder;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @Test
    void shouldAuthenticatedForCorrectUserCredential() throws Exception {
        final var password = "123";
        final var encodePassword = encoder.encode(password);
        given(userRepository.findByName("admin"))
                .willReturn(Optional.of(new User("admin", "{bcrypt}" + encodePassword)));

        mvc.perform(formLogin().user("admin").password(password)).andDo(print()).andExpect(status().isFound())
                .andExpect(authenticated());
    }

    @Test
    void shouldUnauthenticatedForIncorrectRassword() throws Exception {
        given(userRepository.findByName("admin")).willReturn(Optional.of(new User("admin", "{bcrypt}123")));

        mvc.perform(formLogin().user("admin").password("incorrect_password")).andDo(print())
                .andExpect(status().isFound()).andExpect(unauthenticated());
    }

    @Test
    void shouldUnauthenticatedForNotExistedUser() throws Exception {
        given(userRepository.findByName("admin")).willReturn(Optional.empty());

        mvc.perform(formLogin().user("admin").password("incorrect_password")).andDo(print())
                .andExpect(status().isFound()).andExpect(unauthenticated());
    }

}
