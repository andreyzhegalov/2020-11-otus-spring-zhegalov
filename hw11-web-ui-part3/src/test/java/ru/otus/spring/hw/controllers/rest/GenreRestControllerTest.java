package ru.otus.spring.hw.controllers.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ru.otus.spring.hw.controllers.rest.GenreRestController;
import ru.otus.spring.hw.model.Genre;
import ru.otus.spring.hw.repositories.GenreRepository;
import ru.otus.spring.hw.repositories.RepositoryException;

@WebMvcTest(controllers = GenreRestController.class)
public class GenreRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private GenreRepository genreRepository;

    @Captor
    private ArgumentCaptor<Genre> genreCaptor;

    @Test
    void shouldReturnGenreList() throws Exception {
        mvc.perform(get("/api/genres")).andDo(print()).andExpect(status().isOk());
        then(genreRepository).should().findAll();
    }

    @Test
    void shouldAddNewGenre() throws Exception {
        final var genreName = "genre name";
        String genreJson = "{\"name\":\"genre name\"}";
        final var savedGenre = new Genre(genreName);
        savedGenre.setId("132");
        given(genreRepository.save(any())).willReturn(savedGenre);

        mvc.perform(post("/api/genres").content(genreJson).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isCreated());

        then(genreRepository).should().save(genreCaptor.capture());
        assertThat(genreCaptor.getValue().getName()).isEqualTo(genreName);
    }

    @Test
    void shouldNotSaveGenreWithEmptyName() throws Exception {

        String genreJson = "{\"name\":\"\"}";

        mvc.perform(post("/api/genres").content(genreJson).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", is(notNullValue()))).andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errors").isArray()).andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem("Please provide a genre name")));

        then(genreRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldRemoveGenre() throws Exception {
        final var genreId = "123";
        mvc.perform(delete("/api/genres/{id}", genreId)).andDo(print()).andExpect(status().isOk());
        then(genreRepository).should().deleteById(genreId);
    }

    @Test
    void shouldReturnErrorIfDeletedGenreHasBook() throws Exception {
        final var genreId = "id_genre_with_book";
        final var errorMessage = "error";
        doThrow(new RepositoryException(errorMessage)).when(genreRepository).deleteById(genreId);

        mvc.perform(delete("/api/genres/{id}", genreId)).andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", is(errorMessage)));

        then(genreRepository).should().deleteById(genreId);
    }
}
