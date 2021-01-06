package ru.otus.spring.hw.repositories;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.model.Book;

@RequiredArgsConstructor
@Repository
public class BookRepositoryJpa implements BookRepository {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public List<Book> findAll() {
        final EntityGraph<?> entityGraph = em.getEntityGraph("book-authors-genre-entity-graph");
        return em.createQuery("select b from Book b", Book.class).setHint("javax.persistence.fetchgraph", entityGraph)
                .getResultList();
    }

    @Override
    public Optional<Book> findById(long id) {
        final EntityGraph<?> entityGraph = em.getEntityGraph("book-authors-genre-entity-graph");
        final var query = em.createQuery("select b from Book b where b.id=:id", Book.class).setParameter("id", id)
                .setHint("javax.persistence.fetchgraph", entityGraph);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Book save(Book book) {
        if (!book.hasId()) {
            em.persist(book);
            return book;
        } else {
            return em.merge(book);
        }
    }

    @Override
    public void remove(long id) {
        final var rows = em.createQuery("delete from Book b where b.id=:id").setParameter("id", id).executeUpdate();
        if (rows != 1) {
            throw new RepositoryException("Book with id=" + id + " not exist");
        }
    }

}
