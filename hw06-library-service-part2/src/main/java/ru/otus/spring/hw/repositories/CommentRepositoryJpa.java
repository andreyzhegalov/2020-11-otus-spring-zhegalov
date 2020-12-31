package ru.otus.spring.hw.repositories;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.model.Comment;

@RequiredArgsConstructor
@Repository
public class CommentRepositoryJpa implements CommentRepository {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public List<Comment> findAll() {
        return em.createQuery("select c from Comment c", Comment.class).getResultList();
    }

    @Override
    public Optional<Comment> findById(long id) {
        return Optional.ofNullable(em.find(Comment.class, id));
    }

    @Override
    public Comment save(Comment genre) {
        if (!genre.hasId()) {
            em.persist(genre);
            return genre;
        } else {
            return em.merge(genre);
        }
    }

    @Override
    public void remove(long id) {
        try {
            em.remove(em.getReference(Comment.class, id));
        } catch (EntityNotFoundException e) {
            throw new RepositoryException(e);
        }
    }

}
