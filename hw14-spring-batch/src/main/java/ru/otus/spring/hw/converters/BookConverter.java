package ru.otus.spring.hw.converters;

import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;

import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;

public class BookConverter {

    public static Book<ObjectId> convertId(Book<Long> book) {
        final var convertedBook = new Book<ObjectId>();
        convertedBook.setId(new ObjectId());
        convertedBook.setTitle(book.getTitle());
        convertedBook.setGenre(GenreConverter.convertId(book.getGenre()));
        convertedBook.setAuthors(convertAuthorsId(book.getAuthors()));
        return convertedBook;
    }

    private static List<Author<ObjectId>> convertAuthorsId(List<Author<Long>> authorList) {
        return authorList.stream().map(AuthorConverter::convertId).collect(Collectors.toList());
    }
}
