package ru.otus.spring.hw.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.repositories.AuthorRepository;
import ru.otus.spring.hw.repositories.GenreRepository;
import ru.otus.spring.hw.service.BookService;
import ru.otus.spring.hw.service.CommentService;
import ru.otus.spring.hw.service.IOAuthorService;
import ru.otus.spring.hw.service.IOBookService;
import ru.otus.spring.hw.service.IOCommentService;
import ru.otus.spring.hw.service.IOGenreService;

@RequiredArgsConstructor
@ShellComponent
public class ApplicationCommands {
    private final BookService bookService;
    private final CommentService commentService;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;

    private final IOBookService ioBookService;
    private final IOGenreService ioGenreService;
    private final IOAuthorService ioAuthorService;
    private final IOCommentService ioCommentService;

    @ShellMethod(value = "Print all books", key = { "pb", "print-books" })
    public void printAllBooks() {
        final var books = bookService.findAll();
        ioBookService.print(books);
    }

    @ShellMethod(value = "Add new book", key = { "ab", "add-book" })
    public void addBook() {
        final var bookDto = ioBookService.getBook();
        bookService.save(bookDto);
    }

    @ShellMethod(value = "Delete book", key = { "db", "delete-book" })
    public void deleteBook(@ShellOption String id) {
        bookService.deleteBook(id);
    }

    @ShellMethod(value = "Update book", key = { "ub", "update-book" })
    public void updateBook(@ShellOption String id) {
        final var bookDto = ioBookService.getBook();
        bookDto.setId(id);
        bookService.save(bookDto);
    }

    @ShellMethod(value = "Print all genres", key = { "pg", "print-genres" })
    public void printAllGenre() {
        final var genres = genreRepository.findAll();
        ioGenreService.print(genres);
    }

    @ShellMethod(value = "Print all authors", key = { "pa", "print-authors" })
    public void printAllAuthors() {
        final var authors = authorRepository.findAll();
        ioAuthorService.print(authors);
    }

    @ShellMethod(value = "Print all comments", key = { "pc", "print-comments" })
    public void printAllComments() {
        final var comments = commentService.findAll();
        ioCommentService.print(comments);
    }

    @ShellMethod(value = "Add comment to the book", key = { "ac", "add-comment" })
    public void addComment() {
        final var comment = ioCommentService.getComment();
        commentService.addComment(comment);
    }
}
