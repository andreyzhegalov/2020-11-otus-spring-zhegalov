package ru.otus.spring.hw.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import ru.otus.spring.hw.model.Author;

@Component
public class AuthorRepository {


    public List<Author> getByBookId(long bookId)
    {
        return new ArrayList<Author>();
    }
}

