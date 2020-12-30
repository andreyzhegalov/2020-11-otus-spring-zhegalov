package ru.otus.spring.hw.repositories;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import ru.otus.spring.hw.model.Book;

@Repository
public class BookRepositoryJpa implements BookRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Book> findAll() {
        final EntityGraph<?> entityGraph = em.getEntityGraph("book-author-genre-comments-entity-graph");
        return em.createQuery("select b from Book b left join fetch b.comments", Book.class)
                .setHint("javax.persistence.fetchgraph", entityGraph).getResultList();
    }

    @Override
    public Optional<Book> findById(long id) {
        final EntityGraph<?> entityGraph = em.getEntityGraph("book-author-genre-comments-entity-graph");
        final var query = em.createQuery("select b from Book b left join fetch b.comments where b.id=:id", Book.class)
                .setParameter("id", id).setHint("javax.persistence.fetchgraph", entityGraph);
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
        try {
            em.remove(em.getReference(Book.class, id));
        } catch (EntityNotFoundException e) {
            throw new RepositoryException(e);
        }
    }
}
