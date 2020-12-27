package ru.otus.spring.hw.model;

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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "books")
@NoArgsConstructor
@AllArgsConstructor
@Data
@NamedEntityGraph(name = "book-author-genre-entity-graph", attributeNodes = { @NamedAttributeNode("author"),
        @NamedAttributeNode("genre") })
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = true)
    private long id;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne(targetEntity = Author.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToOne(targetEntity = Genre.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "genre_id")
    private Genre genre;
}
