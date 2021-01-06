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
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "books")
@NamedEntityGraph(name = "book-authors-genre-entity-graph", attributeNodes = { @NamedAttributeNode("authors"),
        @NamedAttributeNode("genre") })
@NoArgsConstructor
@Getter
@ToString
public class Book {
    private final static long NOT_EXISTED_ID = 0L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = true)
    private long id;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "book_author", joinColumns = { @JoinColumn(name = "fk_book") }, inverseJoinColumns = {
            @JoinColumn(name = "fk_author") })
    private final Set<Author> authors = new HashSet<>();

    @ManyToOne(targetEntity = Genre.class, fetch = FetchType.EAGER, cascade = { CascadeType.MERGE,
            CascadeType.PERSIST })
    @JoinColumn(name = "genre_id")
    private Genre genre;

    public Book(String title, Author author, Genre genre) {
        this.id = NOT_EXISTED_ID;
        this.title = title;
        addAuthor(author);
        this.genre = genre;
    }

    public Book(long id, String title, Author author, Genre genre) {
        this.id = id;
        this.title = title;
        this.authors.add(author);
        this.genre = genre;
    }

    public void addAuthor(Author author) {
        authors.add(author);
        author.getBooks().add(this);
    }

    public void removeAuthor(Author author) {
        authors.remove(author);
        author.getBooks().remove(this);
    }

    public boolean hasId() {
        return id > NOT_EXISTED_ID;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Book other = (Book) obj;
        return id == other.id;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
