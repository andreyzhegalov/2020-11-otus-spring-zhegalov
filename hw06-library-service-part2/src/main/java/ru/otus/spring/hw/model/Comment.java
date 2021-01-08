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
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "comments")
@NamedEntityGraph(name = "comment-book-entity-graph", attributeNodes = { @NamedAttributeNode(value = "book") })
@Getter
@NoArgsConstructor
public class Comment {
    private final static long NOT_EXISTED_ID = 0L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private long id;

    @Column(name = "text", unique = true)
    @Setter
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
