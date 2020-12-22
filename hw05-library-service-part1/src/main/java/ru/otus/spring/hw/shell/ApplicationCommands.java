package ru.otus.spring.hw.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.service.BookService;
import ru.otus.spring.hw.service.IOBookService;

@RequiredArgsConstructor
@ShellComponent
public class ApplicationCommands {
    private final BookService bookService;
    private final IOBookService ioBookService;

    @ShellMethod(value = "Print all books", key = { "p", "print-books" })
    public void printAllBooks() {
        final var books = bookService.getAllBooks();
        ioBookService.printBooks(books);
    }

    @ShellMethod(value = "Add new book", key = { "a", "add-book" })
    public void addBook() {
        final var book = ioBookService.getBook();
        bookService.saveBook(book);
    }
}
