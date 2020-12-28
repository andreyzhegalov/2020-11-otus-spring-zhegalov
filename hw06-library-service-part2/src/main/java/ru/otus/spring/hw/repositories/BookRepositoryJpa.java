package ru.otus.spring.hw.repositories;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import ru.otus.spring.hw.model.Book;

@Repository
public class BookRepositoryJpa implements BookRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Book> findAll() {
        final var query = em.createQuery("select b from Book b left join fetch b.comments", Book.class);
        final EntityGraph<?> entityGraph = em.getEntityGraph("book-author-genre-entity-graph");
        query.setHint("javax.persistence.fetchgraph", entityGraph);
        return query.getResultList();
    }

    @Override
    public Optional<Book> findById(long id) {
        return Optional.ofNullable(em.find(Book.class, id));
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
        final var query = em.createQuery("delete from Book b where b.id=:id");
        query.setParameter("id", id);
        final var updatedCount = query.executeUpdate();
        if (updatedCount != 1) {
            throw new RepositoryException(String.format("Author with id %d was not deleted", id));
        }
    }
}
