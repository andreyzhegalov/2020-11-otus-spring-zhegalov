package ru.otus.spring.hw.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.model.Comment;

@RequiredArgsConstructor
@Service
public class IOCommentService {
    private final IOService ioService;

    public void print(List<Comment> comments) {
        comments.forEach(b -> ioService.print(b.toString()));
    }

}
