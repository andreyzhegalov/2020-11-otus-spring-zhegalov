package ru.otus.spring.hw.event;

import java.time.Duration;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.repositories.BookRepository;
import ru.otus.spring.hw.repositories.RepositoryException;

@Component
@RequiredArgsConstructor
public class AuthorMongoEventListener extends AbstractMongoEventListener<Author> {

    private final BookRepository bookRepository;

    @Override
    public void onBeforeDelete(@NotNull BeforeDeleteEvent<Author> event) {
        super.onBeforeDelete(event);
        final var source = event.getSource();
        final var authorId = source.get("_id").toString();

        if (bookRepository.existsBookByAuthors_id(authorId).block(Duration.ofMillis(500))) {
            throw new RepositoryException("author can't deleted with existed book");
        }
    }
}
