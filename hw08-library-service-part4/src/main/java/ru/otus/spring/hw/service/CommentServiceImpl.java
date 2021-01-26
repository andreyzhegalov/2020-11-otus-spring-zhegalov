package ru.otus.spring.hw.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.dto.CommentDto;
import ru.otus.spring.hw.model.Comment;
import ru.otus.spring.hw.repositories.BookRepository;
import ru.otus.spring.hw.repositories.CommentRepository;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;

    @Override
    public void addComment(CommentDto commentDto) {
        final var book = bookRepository.findById(commentDto.getBookId())
                .orElseThrow(() -> new ServiceException("book with id " + commentDto.getBookId() + " not exist"));
        final var comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setBook(book);
        commentRepository.save(comment);
    }

    @Override
    public List<CommentDto> findAll() {
        return commentRepository.findAll().stream().map(CommentDto::new).collect(Collectors.toList());
    }
}
