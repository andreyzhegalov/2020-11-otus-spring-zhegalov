package ru.otus.spring.hw.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.dao.AuthorDao;
import ru.otus.spring.hw.dao.BookDao;
import ru.otus.spring.hw.dao.GenreDao;
import ru.otus.spring.hw.service.IOAuthorService;
import ru.otus.spring.hw.service.IOBookService;
import ru.otus.spring.hw.service.IOGenreService;

@RequiredArgsConstructor
@ShellComponent
public class ApplicationCommands {
    private final BookDao bookDao;
    private final GenreDao genreDao;
    private final AuthorDao authorDao;
    private final IOBookService ioBookService;
    private final IOGenreService ioGenreService;
    private final IOAuthorService ioAuthorService;

    @ShellMethod(value = "Print all books", key = { "pb", "print-books" })
    public void printAllBooks() {
        final var books = bookDao.getAll();
        ioBookService.print(books);
    }

    @ShellMethod(value = "Add new book", key = { "ab", "add-book" })
    public void addBook() {
        final var bookDto = ioBookService.get();
        bookDao.insertBook(bookDto);
    }

    @ShellMethod(value = "Delete book", key = { "db", "delete-book" })
    public void deleteBook(@ShellOption long id) {
        bookDao.deleteBook(id);
    }

    @ShellMethod(value = "Update book", key = { "ub", "update-book" })
    public void updateBook(@ShellOption long id) {
        final var bookDto = ioBookService.get();
        bookDto.setId(id);
        bookDao.updateBook(bookDto);
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
