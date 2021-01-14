package ru.otus.spring.hw.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.otus.spring.hw.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = { "book" })
    List<Comment> findAll();

    @Modifying
    @Query("delete from Comment c where c.id=:id")
    void deleteById(@Param("id") Long id);
}
