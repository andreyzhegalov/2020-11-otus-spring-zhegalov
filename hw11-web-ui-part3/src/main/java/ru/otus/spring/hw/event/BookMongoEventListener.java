package ru.otus.spring.hw.event;

import java.util.Arrays;

import org.jetbrains.annotations.NotNull;
import org.mockito.internal.util.collections.Sets;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.repositories.AuthorRepository;
import ru.otus.spring.hw.repositories.CommentRepository;
import ru.otus.spring.hw.repositories.RepositoryException;

@Component
@RequiredArgsConstructor
public class BookMongoEventListener extends AbstractMongoEventListener<Book> {

    private final AuthorRepository authorRepository;

    private final CommentRepository commentRepository;

    private Author updateAuthorBooks(Author author, Book book) {
        final var bookSet = Sets.newSet(author.getBooks().toArray(new Book[0]));
        bookSet.add(book);
        author.setBooks(Arrays.asList(bookSet.toArray(new Book[0])));
        return author;
    }

    @Override
    public void onBeforeConvert(@NotNull BeforeConvertEvent<Book> event) {
        super.onBeforeConvert(event);
        final var book = event.getSource();

        book.getAuthors().forEach(a -> {
            if (!authorRepository.existsById(a.getId()).block()) {
                throw new RepositoryException("author with id " + a.getId() + " not exist");
            }
        });
    }

    @Override
    public void onAfterSave(@NotNull AfterSaveEvent<Book> event) {
        super.onAfterSave(event);
        final var book = event.getSource();
        Flux.fromIterable(book.getAuthors()).flatMap(a -> authorRepository.findById(a.getId()))
                .map(a -> updateAuthorBooks(a, book)).flatMap(authorRepository::save).subscribe();
    }

    @Override
    public void onAfterDelete(@NotNull AfterDeleteEvent<Book> event) {
        super.onAfterDelete(event);
        final var source = event.getSource();
        final var id = source.get("_id").toString();
        authorRepository.removeBookArrayElementsById(id);
        commentRepository.removeAllByBook_id(id);
    }
}
