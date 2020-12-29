package ru.otus.spring.hw.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.model.Comment;

@RequiredArgsConstructor
@Service
public class IOCommentService {
    private static final String GET_COMMENT_MESSAGE = "Введите комментарий:";
    private final IOService ioService;

    public void print(List<Comment> comments) {
        comments.forEach(b -> ioService.print(b.toString()));
    }

    public Comment getComment() {
        ioService.print(GET_COMMENT_MESSAGE);
        final var text = ioService.read();
        return new Comment(text);
    }

}
