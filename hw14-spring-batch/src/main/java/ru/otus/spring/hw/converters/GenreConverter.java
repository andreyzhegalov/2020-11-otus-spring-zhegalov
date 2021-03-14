package ru.otus.spring.hw.converters;

import org.bson.types.ObjectId;

import ru.otus.spring.hw.model.Genre;

public class GenreConverter {

    public static Genre<ObjectId> convertId(Genre<Long> init) {
        final var converted = new Genre<ObjectId>();
        converted.setId(new ObjectId());
        converted.setName(init.getName());
        return converted;
    }

}

