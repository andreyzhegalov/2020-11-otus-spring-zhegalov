package ru.otus.spring.hw.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "books")
@NamedEntityGraph(name = "book-author-genre-comments-entity-graph", attributeNodes = { @NamedAttributeNode("author"),
        @NamedAttributeNode("genre"), @NamedAttributeNode("comments") })
@NoArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class Book {
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = true)
    private long id;

    @Setter
    @Column(name = "title", nullable = false)
    private String title;

    @Setter
    @ManyToOne(targetEntity = Author.class, fetch = FetchType.EAGER, cascade = { CascadeType.MERGE,
            CascadeType.PERSIST })
    @JoinColumn(name = "author_id")
    private Author author;

    @Setter
    @ManyToOne(targetEntity = Genre.class, fetch = FetchType.EAGER, cascade = { CascadeType.MERGE,
            CascadeType.PERSIST })
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public Book(long id, String title, Author author, Genre genre) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.comments = new ArrayList<>();
    }

    public boolean addComment(Comment comment) {
        comment.setBook(this);
        return comments.add(comment);
    }

    public boolean hasId() {
        return id > 0L;
    }
}
