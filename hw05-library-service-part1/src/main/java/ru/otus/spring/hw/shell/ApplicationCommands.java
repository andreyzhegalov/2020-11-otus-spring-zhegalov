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
    private final IOBookService ioBoolService;

    @ShellMethod(value = "Print all books", key = { "p", "print-books" })
    public void printAllBooks() {
        final var books = bookService.getAllBooks();
        ioBoolService.printBooks(books);
    }

    @ShellMethod(value = "Add new book", key = { "a", "add-book" })
    public void addBook() {
        final var book = ioBoolService.getBook();
        bookService.saveBook(book);
    }
}
