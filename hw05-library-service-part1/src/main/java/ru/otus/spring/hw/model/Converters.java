package ru.otus.spring.hw.model;

import ru.otus.spring.hw.model.dto.BookDto;

public class Converters {

    public static BookDto convert(Book book){
        return new BookDto(book.getId(), book.getTitle(), book.getAuthor().getId());
    }

    public static Book convert(BookDto bookDto, Author author){
        return new Book(bookDto.getId(), bookDto.getTitle(), author);
    }
}

