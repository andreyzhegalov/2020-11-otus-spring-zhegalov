package ru.otus.spring.hw.event;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.repositories.AuthorRepository;

@Component
@RequiredArgsConstructor
public class BookMongoEventListener extends AbstractMongoEventListener<Book> {

    private final AuthorRepository authorRepository;

    @Override
    public void onAfterDelete(AfterDeleteEvent<Book> event) {
        super.onAfterDelete(event);
        final var source = event.getSource();
        final var id = source.get("_id").toString();

        authorRepository.removeBookArrayElementsById(id);
    }
}
