package ru.otus.spring.hw.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "genres")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Genre {
    private final static long NOT_EXISTED_ID = 0L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private long id;

    @Column(name = "name", nullable = false, unique = true)
    @Setter
    private String name;

    public Genre(String name) {
        this.id = NOT_EXISTED_ID;
        this.name = name;
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
        Genre other = (Genre) obj;
        return id == other.id;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
