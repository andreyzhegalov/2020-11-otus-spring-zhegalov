package ru.otus.spring.hw.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void addComment(CommentDto commentDto) {
        final var book = bookRepository.findById(commentDto.getBookId())
                .orElseThrow(() -> new ServiceException("book with id " + commentDto.getBookId() + " not exist"));
        commentRepository.save(new Comment(commentDto.getText(), book));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> findAll() {
        return commentRepository.findAll().stream().map(CommentDto::new).collect(Collectors.toList());
    }
}
