package ru.otus.spring.hw.converters;

import org.bson.types.ObjectId;

import ru.otus.spring.hw.model.Author;

public class AuthorConverter {

    public static Author<ObjectId> convertId(Author<Long> init) {
        final var converted = new Author<ObjectId>();
        converted.setId(new ObjectId());
        converted.setName(init.getName());
        return converted;
    }

}
