package ru.otus.spring.hw.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.dao.AuthorDao;
import ru.otus.spring.hw.dao.GenreDao;
import ru.otus.spring.hw.model.Author;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.model.Genre;
import ru.otus.spring.hw.service.BookService;
import ru.otus.spring.hw.service.IOModelService;

@RequiredArgsConstructor
@ShellComponent
public class ApplicationCommands {
    private final BookService bookService;
    private final GenreDao genreDao;
    private final AuthorDao authorDao;
    private final IOModelService<Book> ioBookService;
    private final IOModelService<Genre> ioGenreService;
    private final IOModelService<Author> ioAuthorService;

    @ShellMethod(value = "Print all books", key = { "p", "print-books" })
    public void printAllBooks() {
        final var books = bookService.getAllBooks();
        ioBookService.print(books);
    }

    @ShellMethod(value = "Add new book", key = { "a", "add-book" })
    public void addBook() {
        final var book = ioBookService.get();
        bookService.saveBook(book);
    }

    @ShellMethod(value = "Print all genres", key = { "pg", "print-genres" })
    public void printAllGenre() {
        final var genres = genreDao.getAll();
        ioGenreService.print(genres);
    }

    @ShellMethod(value = "Print all authors", key = { "pa", "print-authors" })
    public void printAllAuthors() {
        final var authors = authorDao.getAll();
        ioAuthorService.print(authors);
    }
}
