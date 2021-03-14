package ru.otus.spring.hw.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;

import lombok.NoArgsConstructor;
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;

@NoArgsConstructor
public class BookMongo extends Book<ObjectId> {

    public BookMongo(BookDb book) {
        this.setId(new ObjectId());
        this.setTitle(book.getTitle());
        this.setGenre(book.getGenre());
        this.setAuthors(convertAuthorsId(book.getAuthors()));
    }

    private List<Author<ObjectId>> convertAuthorsId(List<Author<Long>> authorList) {
        return authorList.stream().map(a -> new AuthorMongo((AuthorDb) a)).collect(Collectors.toList());
    }
}
