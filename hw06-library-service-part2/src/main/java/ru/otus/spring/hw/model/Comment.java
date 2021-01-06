package ru.otus.spring.hw.model;

import java.util.Objects;

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
import javax.persistence.NamedSubgraph;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comments")
@NamedEntityGraph(name = "comment-book-entity-graph", attributeNodes = @NamedAttributeNode(value = "book", subgraph = "genre-subgraph"), subgraphs = {
        @NamedSubgraph(name = "genre-subgraph", attributeNodes = { @NamedAttributeNode("genre") }) })
@Data
@NoArgsConstructor
public class Comment {
    private final static long NOT_EXISTED_ID = 0L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = true, unique = true)
    private long id;

    @Column(name = "text", nullable = true, unique = true)
    private String text;

    @ManyToOne(targetEntity = Book.class, fetch = FetchType.EAGER, cascade = { CascadeType.MERGE })
    @JoinColumn(name = "book_id")
    private Book book;

    public Comment(String text, Book book) {
        Objects.requireNonNull(book);
        this.id = NOT_EXISTED_ID;
        this.text = text;
        this.book = book;
    }

    // TODO delete this
    public Comment(long id, String text) {
        this.id = id;
        this.text = text;
    }

    public Comment(long id, String text, Book book) {
        this.id = id;
        this.text = text;
        this.book = book;
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
        Comment other = (Comment) obj;
        return id == other.id;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Comment [book=" + book + ", id=" + id + ", text=" + text + "]";
    }
}
