package ru.otus.spring.hw.batch;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.model.Genre;

public class BookIdConvertProcessorTest {
    private final BookIdConvertProcessor bookIdConvertProcessor = new BookIdConvertProcessor();

    @Test
    void convertedBookShouldContainIdOfObjectIdType(){
        final var author = new Author<Long>();
        author.setId(3L);
        final var genre = new Genre<Long>();
        genre.setId(2L);
        final var book = new Book<Long>();
        book.setId(1L);
        book.setGenre(genre);
        book.setAuthors(Collections.singletonList(author));

        final var converterBook = bookIdConvertProcessor.process(book);
        assertThat(converterBook).isNotNull();
        assertThat(converterBook.getId()).isInstanceOf(ObjectId.class);
        assertThat(converterBook.getGenre().getId()).isInstanceOf(ObjectId.class);
        converterBook.getAuthors().forEach(a -> {
            assertThat(a.getId()).isInstanceOf(ObjectId.class);
        });
    }

    @Test
    void shouldCorrectlyConvertGenreIdForSameGenre() {
        final var genre = new Genre<Long>();
        genre.setId(2L);

        final var book = new Book<Long>();
        book.setId(1L);
        book.setGenre(genre);

        final var bookWithSameGenre = new Book<Long>();
        bookWithSameGenre.setId(2L);
        bookWithSameGenre.setGenre(genre);

        final var converterBook = bookIdConvertProcessor.process(book);
        final var converterBookWithSameGenre = bookIdConvertProcessor.process(bookWithSameGenre);
        assertThat(converterBook).isNotNull();
        assertThat(converterBookWithSameGenre).isNotNull();
        assertThat(converterBook.getGenre().getId()).isInstanceOf(ObjectId.class);
        assertThat(converterBook.getGenre().getId()).isEqualTo(converterBookWithSameGenre.getGenre().getId());
    }

    @Test
    void shouldCorrectlyConvertGenreIdForDiferentsGenre() {
        final var genre11 = new Genre<Long>();
        genre11.setId(11L);
        final var genre12 = new Genre<Long>();
        genre12.setId(12L);
        final var bookWithGenre11 = new Book<Long>();
        bookWithGenre11.setId(1L);
        bookWithGenre11.setGenre(genre11);
        final var bookWithGenre12 = new Book<Long>();
        bookWithGenre12.setId(2L);
        bookWithGenre12.setGenre(genre12);

        final var converterBookWithGenre11 = bookIdConvertProcessor.process(bookWithGenre11);
        final var converterBookWithGenre12 = bookIdConvertProcessor.process(bookWithGenre12);

        assertThat(converterBookWithGenre11).isNotNull();
        assertThat(converterBookWithGenre12).isNotNull();
        assertThat(converterBookWithGenre11.getGenre().getId()).isNotEqualTo(converterBookWithGenre12.getGenre().getId());
    }

    @Test
    void shouldCorrectlyConvertAuthorIdForSameAuthor() {
        final var author = new Author<Long>();
        author.setId(3L);
        final var genre = new Genre<Long>();
        genre.setId(2L);
        final var book = new Book<Long>();
        book.setId(1L);
        book.setGenre(genre);
        book.setAuthors(Collections.singletonList(author));
        final var bookWithSameAuthor = new Book<Long>();
        bookWithSameAuthor.setId(2L);
        bookWithSameAuthor.setGenre(genre);
        bookWithSameAuthor.setAuthors(Collections.singletonList(author));

        final var converterBook = bookIdConvertProcessor.process(book);
        final var converterBookWithSameAuthor = bookIdConvertProcessor.process(bookWithSameAuthor);

        assertThat(converterBook).isNotNull();
        assertThat(converterBookWithSameAuthor).isNotNull();
        assertThat(converterBook.getAuthors()).hasSameElementsAs(converterBookWithSameAuthor.getAuthors());
    }

    @Test
    void shouldCorrectlyConvertAuthorIdForDiferentsAuthor() {
        final var author11 = new Author<Long>();
        author11.setId(11L);
        final var author12 = new Author<Long>();
        author11.setId(12L);
        final var genre = new Genre<Long>();
        genre.setId(2L);
        final var bookWithAuthor11 = new Book<Long>();
        bookWithAuthor11.setId(1L);
        bookWithAuthor11.setGenre(genre);
        bookWithAuthor11.setAuthors(Collections.singletonList(author11));
        final var bookWithAuthor12 = new Book<Long>();
        bookWithAuthor12.setId(2L);
        bookWithAuthor12.setGenre(genre);
        bookWithAuthor12.setAuthors(Collections.singletonList(author12));

        final var converterBookWithAuthor11 = bookIdConvertProcessor.process(bookWithAuthor11);
        final var converterBookWithAuthor12 = bookIdConvertProcessor.process(bookWithAuthor12);

        assertThat(converterBookWithAuthor11).isNotNull();
        assertThat(converterBookWithAuthor12).isNotNull();
        converterBookWithAuthor11.getAuthors().forEach(author->{
            assertThat(author).isNotIn(converterBookWithAuthor12.getAuthors());
        });
    }

}
