package ru.otus.spring.hw.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import ru.otus.spring.hw.dto.GenreDto;
import ru.otus.spring.hw.repositories.RepositoryException;
import ru.otus.spring.hw.service.GenreService;

@WebMvcTest(controllers = GenreController.class)
public class GenreControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private GenreService genreService;

    @Captor
    private ArgumentCaptor<GenreDto> captor;

    @Test
    void shouldReturnAllGenre() throws Exception {
        mvc.perform(get("/genres")).andDo(print()).andExpect(status().isOk())
                .andExpect(model().attributeExists("genres")).andExpect(view().name("genres"));
        then(genreService).should().findAll();
    }

    @Test
    void shouldNotSaveGenreWithEmptyName() throws Exception {
        mvc.perform(post("/genres").param("name", "")).andDo(print()).andExpect(status().isBadRequest())
                .andExpect(content().string(not(emptyString())));
        then(genreService).shouldHaveNoInteractions();
    }

    @Test
    void shouldAddNewGenre() throws Exception {
        final var genreName = "genre name";
        mvc.perform(post("/genres").param("name", "genre name")).andDo(print()).andExpect(status().isFound())
                .andExpect(view().name("redirect:/genres"));

        then(genreService).should().saveGenreDto(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo(genreName);
    }

    @Test
    void shouldRemoveGenre() throws Exception {
        final var genreId = "123";
        mvc.perform(delete("/genres").param("id", genreId)).andDo(print()).andExpect(status().isFound())
                .andExpect(view().name("redirect:/genres"));
        then(genreService).should().deleteById(genreId);
    }

    @Test
    void shouldNotRemoveGenreIfIdEmpty() throws Exception {
        mvc.perform(delete("/genres").param("id", "")).andDo(print()).andExpect(status().isBadRequest())
                .andExpect(content().string(not(emptyString())));
        then(genreService).shouldHaveNoInteractions();
    }

    @Test
    void shouldReturnErrorIfDeletedGenreHasBook() throws Exception {
        final var genreId = "id_genre_with_book";
        doThrow(new RepositoryException("error message")).when(genreService).deleteById(genreId);

        mvc.perform(delete("/genres").param("id", genreId)).andDo(print()).andExpect(status().isBadRequest())
                .andExpect(content().string(not(emptyString())));

        then(genreService).should().deleteById(genreId);
    }
}
