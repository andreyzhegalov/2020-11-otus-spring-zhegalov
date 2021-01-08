package ru.otus.spring.hw.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.dto.CommentDto;

@RequiredArgsConstructor
@Service
public class IOCommentService {
    private static final String GET_COMMENT_MESSAGE = "Введите комментарий:";
    private static final String GET_BOOK_ID = "Введите идентификатор книги";
    private final IOService ioService;

    public void print(List<CommentDto> comments) {
        comments.forEach(b -> ioService.print(b.toString()));
    }

    public CommentDto getComment() {
        ioService.print(GET_BOOK_ID);
        final var bookId = Long.parseLong(ioService.read());
        ioService.print(GET_COMMENT_MESSAGE);
        final var text = ioService.read();
        return new CommentDto(text, bookId);
    }
}
