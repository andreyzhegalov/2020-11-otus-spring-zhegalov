package ru.otus.spring.hw.repositories;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
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
        final EntityGraph<?> entityGraph = em.getEntityGraph("comment-book-entity-graph");
        return em.createQuery("select c from Comment c", Comment.class)
                .setHint("javax.persistence.fetchgraph", entityGraph).getResultList();
    }

    @Override
    public Optional<Comment> findById(long id) {
        return Optional.ofNullable(em.find(Comment.class, id));
    }

    @Override
    public Comment save(Comment comment) {
        if (!comment.getBook().hasId()) {
            throw new RepositoryException("comment cannot be added to non-existent book");
        }
        if (!comment.hasId()) {
            em.persist(comment);
            return comment;
        } else {
            return em.merge(comment);
        }
    }

    @Override
    public void remove(long id) {
        final var deletedRows = em.createQuery("delete from Comment c where c.id=:id").setParameter("id", id)
                .executeUpdate();
        if (deletedRows != 1) {
            throw new RepositoryException("Comment with id = " + id + " not exist");
        }
    }

    @Override
    public void deleteByBookId(long bookId) {
        em.createQuery("delete from Comment c where c.book.id=:bookId").setParameter("bookId", bookId).executeUpdate();
    }
}
