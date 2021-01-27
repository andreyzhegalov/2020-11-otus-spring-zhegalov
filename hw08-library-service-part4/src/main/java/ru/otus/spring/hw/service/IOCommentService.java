package ru.otus.spring.hw.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.dto.CommentDto;

@RequiredArgsConstructor
@Service
public class IOCommentService {
    private static final String GET_BOOK_ID = "Введите идентификатор книги";
    private static final String GET_COMMENT_MESSAGE = "Введите комментарий:";
    private final IOService ioService;

    public void print(List<CommentDto> comments) {
        comments.forEach(c -> printComment(c));
    }

    private void printComment(CommentDto comment) {
        final var sb = new StringBuffer();
        sb.append("book id: " + comment.getBookId());
        sb.append("; ");
        sb.append("book title: " + comment.getBookTitle());
        sb.append("; ");
        sb.append("text: " + comment.getText());
        ioService.print(sb.toString());
    }

    public CommentDto getComment() {
        ioService.print(GET_BOOK_ID);
        final var bookId = ioService.read();
        ioService.print(GET_COMMENT_MESSAGE);
        final var text = ioService.read();
        return new CommentDto(text, bookId);
    }
}
