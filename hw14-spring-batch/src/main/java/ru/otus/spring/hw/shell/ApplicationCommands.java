package ru.otus.spring.hw.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ShellComponent
public class ApplicationCommands {

    @ShellMethod(value = "Print all books", key = { "pb", "print-books" })
    public void printAllBooks() {
        // final var books = bookService.findAll();
        // ioBookService.print(books);
    }

}
