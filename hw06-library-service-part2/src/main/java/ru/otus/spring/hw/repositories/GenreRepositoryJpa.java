package ru.otus.spring.hw.repositories;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import ru.otus.spring.hw.dao.DaoException;
import ru.otus.spring.hw.model.Genre;

@Repository
public class GenreRepositoryJpa implements GenreRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Genre> findAll() {
        return em.createQuery("select g from Genre g", Genre.class).getResultList();
    }

    @Override
    public Optional<Genre> findById(long id) {
        return Optional.ofNullable(em.find(Genre.class, id));
    }

    @Override
    public Genre save(Genre genre) {
        if (genre.getId() <= 0) {
            em.persist(genre);
            return genre;
        } else {
            return em.merge(genre);
        }
    }

    @Override
    public void remove(long id) {
        final var query = em.createQuery("delete from Genre g where id=:id");
        query.setParameter("id", id);
        final var result = query.executeUpdate();
        if (result == 0) {
            throw new DaoException(String.format("Genre with id %d was not deleted", id));
        }
    }

}
