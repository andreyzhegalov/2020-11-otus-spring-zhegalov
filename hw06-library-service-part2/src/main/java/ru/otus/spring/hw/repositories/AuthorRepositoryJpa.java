package ru.otus.spring.hw.repositories;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.model.Author;

@Repository
@RequiredArgsConstructor
public class AuthorRepositoryJpa implements AuthorRepository {

    @PersistenceContext
    private final EntityManager em;

    @Transactional(readOnly = true)
    @Override
    public List<Author> findAll() {
        return em.createQuery("select a from Author a", Author.class).getResultList();
    }

    @Override
    public Optional<Author> findById(long id) {
        return Optional.ofNullable(em.find(Author.class, id));
    }

    @Override
    public Author save(Author author) {
        if (!author.hasId()) {
            em.persist(author);
            return author;
        } else {
            return em.merge(author);
        }
    }

    @Override
    public void remove(long id) {
        try {
            em.remove(em.getReference(Author.class, id));
        } catch (EntityNotFoundException e) {
            throw new RepositoryException(e);
        }
    }
}
