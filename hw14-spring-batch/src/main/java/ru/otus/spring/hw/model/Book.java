package ru.otus.spring.hw.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "books")
@NoArgsConstructor
@Data
public class Book {
    private final static long NOT_EXISTED_ID = 0L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private long id = NOT_EXISTED_ID;

    @Column(name = "title", nullable = false)
    private String title;

    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "book_author", joinColumns = { @JoinColumn(name = "book_id") }, inverseJoinColumns = {
            @JoinColumn(name = "author_id") })
    private final Set<Author> authors = new HashSet<>();

    @ManyToOne(targetEntity = Genre.class, fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST,
            CascadeType.MERGE })
    @JoinColumn(name = "genre_id")
    private Genre genre;

    public Book(long id, String title, Genre genre) {
        this.id = id;
        this.title = title;
        this.genre = genre;
    }

    public void addAuthor(Author author) {
        authors.add(author);
    }

    public void removeAuthor(Author author) {
        authors.remove(author);
    }

    public boolean hasId() {
        return id > NOT_EXISTED_ID;
    }
}
