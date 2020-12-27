package ru.otus.spring.hw.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.fail;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import ru.otus.spring.hw.dao.DaoException;
import ru.otus.spring.hw.model.Author;

@DataJpaTest
@Import(AuthorRepositoryJpa.class)
public class AuthorRepositoryJpaTest {
    private final static long EXISTED_AUTHOR_ID = 1L;
    private final static long NOT_EXISTED_AUTHOR_ID = 3L;
    private final static int AUTHOR_COUNT = 2;

    @Autowired
    private AuthorRepositoryJpa authorRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    void shouldReturnAuthorList() {
        final SessionFactory sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        var authors = authorRepository.findAll();
        assertThat(authors).isNotNull().hasSize(AUTHOR_COUNT).allMatch(s -> !s.getName().equals(""));

        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(1);
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
    void shouldUpdateAuthorIfIdExist() {
        final var initAuthor = authorRepository.findById(EXISTED_AUTHOR_ID).orElseGet(() -> fail("author not exist"));
        final var updatedAuthor = new Author(initAuthor.getId(), initAuthor.getName() + "_modify");

        assertThatCode(() -> authorRepository.save(updatedAuthor)).doesNotThrowAnyException();

        assertThat(authorRepository.findById(EXISTED_AUTHOR_ID)).isPresent().get().isEqualTo(updatedAuthor);
        assertThat(authorRepository.findAll()).hasSize(AUTHOR_COUNT);
    }

    @Test
    void shouldInsertIfAuthorIdNotExisted() {
        final var updatedAuthor = new Author(NOT_EXISTED_AUTHOR_ID, "name");
        final var author = authorRepository.save(updatedAuthor);
        assertThat(authorRepository.findAll()).hasSize(AUTHOR_COUNT + 1);
        assertThat(author).isEqualTo(updatedAuthor);
    }

    @Test
    void deletingAExistingAuthorShouldDeleteAuthor() {
        authorRepository.remove(EXISTED_AUTHOR_ID);

        assertThat(authorRepository.findById(EXISTED_AUTHOR_ID)).isNotPresent();
        assertThat(authorRepository.findAll()).hasSize(AUTHOR_COUNT - 1);
    }

    @Test
    void deletingANonExistingAuthorSouldThrowAnException() {
        assertThatCode(() -> authorRepository.remove(NOT_EXISTED_AUTHOR_ID)).isInstanceOf(DaoException.class);
        assertThat(authorRepository.findAll()).hasSize(AUTHOR_COUNT);
    }
}
