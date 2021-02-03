package ru.otus.spring.hw.event;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.model.Genre;
import ru.otus.spring.hw.repositories.BookRepository;
import ru.otus.spring.hw.repositories.RepositoryException;

@RequiredArgsConstructor
@Component
public class GenreRepositoryListener extends AbstractMongoEventListener<Genre> {

    private final BookRepository bookRepository;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Genre> event) {
        super.onBeforeDelete(event);
        final var source = event.getSource();
        final var genreId = source.get("_id").toString();

        if (bookRepository.existsBookByGenre_id(genreId)) {
            throw new RepositoryException("genre can't deleted in existed book");
        }
    }
}
