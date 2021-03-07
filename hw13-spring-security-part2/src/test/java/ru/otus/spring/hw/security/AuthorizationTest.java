package ru.otus.spring.hw.security;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;




import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import ru.otus.spring.hw.model.User;
import ru.otus.spring.hw.repositories.AbstractRepositoryTest;
import ru.otus.spring.hw.repositories.AuthorRepository;
import ru.otus.spring.hw.repositories.UserRepository;

@WebMvcTest()
@AutoConfigureDataMongo
@ComponentScan({ "ru.otus.spring.hw.security", "ru.otus.spring.hw.service" })
public class AuthorizationTest {
    private static final String USER_INPUT = "custom_name";
    private static final String PASSWORD_INPUT = "custom_password";

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
        given(userRepository.findByName("admin")).willReturn(Optional.of(new User("admin", encodePassword)));

        mvc.perform(formLogin().user(USER_INPUT, "admin").password(PASSWORD_INPUT, password)).andDo(print())
                .andExpect(status().isFound()).andExpect(authenticated());
    }

    @Test
    @WithMockUser(username = "admin")
    public void shouldEnabledAccessToAllUriForAdmin() throws Exception {
        this.mvc.perform(get("/books")).andDo(print()).andExpect(status().isOk());
            // .andExpect(jsonPath("$.user.privileges[0].name").value("FOO_READ_PRIVILEGE"))
            // .andExpect(jsonPath("$.user.organization.name").value("FirstOrg"))
            // .andExpect(jsonPath("$.user.username").value("john"));
    }
}
