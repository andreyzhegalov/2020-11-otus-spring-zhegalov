package ru.otus.spring.hw.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import ru.otus.spring.hw.model.Author;

@DataJpaTest
@Import(AuthorRepositoryJpa.class)
public class AuthorRepositoryJpaTest {
    private final static long EXISTED_AUTHOR_ID = 1L;
    private final static long NOT_EXISTED_AUTHOR_ID = 4L;
    private final static int AUTHOR_COUNT = 3;

    @Autowired
    private AuthorRepositoryJpa authorRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    void shouldReturnAuthorList() {
        var authors = authorRepository.findAll();
        assertThat(authors).isNotNull().hasSize(AUTHOR_COUNT).allMatch(s -> s != null && !s.getName().equals(""));
    }

    @Test
    void shouldReturnAuthorByIdWhenAuthorExisted() {
        assertThat(authorRepository.findById(EXISTED_AUTHOR_ID)).isPresent().get().extracting("id")
                .isEqualTo(EXISTED_AUTHOR_ID);
    }

    @Test
    void shouldNotReturnAuthorByIdForNotExistingAuthor() {
        assertThat(authorRepository.findById(NOT_EXISTED_AUTHOR_ID)).isNotPresent();
    }

    @Test
    void shouldInsertIfAuthorIdNotExisted() {
        final var insertedAuthor = new Author("name");
        assertThat(insertedAuthor.hasId()).isFalse();

        final var author = authorRepository.save(insertedAuthor);
        em.flush();

        assertThat(author.hasId()).isTrue();
        assertThat(authorRepository.findAll()).hasSize(AUTHOR_COUNT + 1);
    }

    @Test
    void shouldUpdateAuthorIfIdExist() {
        final var author = authorRepository.findById(EXISTED_AUTHOR_ID).orElseGet(() -> fail("author not exist"));
        final var updatedAuthor = new Author(author.getId(), author.getName() + "_modify");

        assertThatCode(() -> authorRepository.save(updatedAuthor)).doesNotThrowAnyException();
        em.flush();
        em.clear();

        assertThat(authorRepository.findById(EXISTED_AUTHOR_ID)).isPresent().get().isEqualTo(updatedAuthor);
        assertThat(authorRepository.findAll()).hasSize(AUTHOR_COUNT);
    }

    @Test
    void deletingAExistingAuthorShouldDeleteAuthor() {
        authorRepository.findById(EXISTED_AUTHOR_ID).orElseGet(() -> fail("author not exist"));
        em.clear();

        authorRepository.remove(EXISTED_AUTHOR_ID);
        em.flush();
        em.clear();

        assertThat(authorRepository.findById(EXISTED_AUTHOR_ID)).isNotPresent();
        assertThat(authorRepository.findAll()).hasSize(AUTHOR_COUNT - 1);
    }

    @Test
    void deletingANonExistingAuthorShouldThrowAnException() {
        assertThatCode(() -> authorRepository.remove(NOT_EXISTED_AUTHOR_ID)).isInstanceOf(RepositoryException.class);
        assertThat(authorRepository.findAll()).hasSize(AUTHOR_COUNT);
    }
}
