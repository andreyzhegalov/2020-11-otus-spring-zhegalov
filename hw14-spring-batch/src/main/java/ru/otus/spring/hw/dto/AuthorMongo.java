package ru.otus.spring.hw.dto;

import org.bson.types.ObjectId;

import lombok.NoArgsConstructor;
import ru.otus.spring.hw.model.Author;

@NoArgsConstructor
public class AuthorMongo extends Author<ObjectId> {

    public AuthorMongo(AuthorDb author){
        this.setId(new ObjectId());
        this.setName(author.getName());
    }
}

