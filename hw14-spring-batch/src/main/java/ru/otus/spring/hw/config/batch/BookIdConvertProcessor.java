package ru.otus.spring.hw.config.batch;

import java.util.HashMap;
import java.util.Map;

import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.converters.BookConverter;
import ru.otus.spring.hw.model.Book;

@StepScope
@RequiredArgsConstructor
@Component
public class BookIdConvertProcessor implements ItemProcessor<Book<Long>, Book<ObjectId>> {
    private final Map<Long, ObjectId> genreIdMap = new HashMap<>();
    private final Map<Long, ObjectId> authorIdMap = new HashMap<>();

    @Override
    public Book<ObjectId> process(@NotNull Book<Long> book) {
        var convertedBook = BookConverter.convertId(book);
        genreIdMap.putIfAbsent(book.getGenre().getId(), convertedBook.getGenre().getId());
        convertedBook.getGenre().setId(genreIdMap.get(book.getGenre().getId()));
        updateAuthorId(book, convertedBook);

        return convertedBook;
    }

    private void updateAuthorId(Book<Long> initBook, Book<ObjectId> convertedBook) {
        for (int i = 0; i < initBook.getAuthors().size(); i++) {
            final var author = initBook.getAuthors().get(i);
            final var convertedAuthor = convertedBook.getAuthors().get(i);
            authorIdMap.putIfAbsent(author.getId(), convertedAuthor.getId());
            convertedAuthor.setId(authorIdMap.get(author.getId()));
        }
    }
}
