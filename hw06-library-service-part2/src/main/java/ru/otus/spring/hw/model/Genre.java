package ru.otus.spring.hw.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "genres")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Genre {
    private final static long NOT_EXISTED_ID = 0L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = true)
    private long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    public Genre(String name) {
        this.id = NOT_EXISTED_ID;
        this.name = name;
    }

    public boolean hasId() {
        return id > NOT_EXISTED_ID;
    }
}
