package ru.otus.spring.hw.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private final static long NOT_EXISTED_ID = 0L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = true, unique = true)
    private long id;

    @Column(name = "text", nullable = true, unique = true)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    private Book book;

    public Comment(String text) {
        this.id = NOT_EXISTED_ID;
        this.text = text;
    }

    public Comment(long id, String text) {
        this.id = id;
        this.text = text;
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
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Comment [ id=" + id + ", text=" + text + "]";
    }

}
