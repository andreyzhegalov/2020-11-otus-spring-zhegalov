package ru.otus.spring.hw.model;

import org.bson.types.ObjectId;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AuthorMongo extends Author<ObjectId>{

    public AuthorMongo(AuthorDb author){
        this.setId(new ObjectId());
        this.setName(author.getName());
    }
}

