package ru.otus.spring.hw.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.controllers.dto.CommentDto;
import ru.otus.spring.hw.model.Comment;
import ru.otus.spring.hw.repositories.BookRepository;
import ru.otus.spring.hw.repositories.CommentRepository;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;

    @Transactional
    @Override
    public CommentDto addComment(CommentDto commentDto) {
        final var book = bookRepository.findById(commentDto.getBookId())
                .orElseThrow(() -> new ServiceException("book with id " + commentDto.getBookId() + " not exist"));
        final var comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setBook(book);
        final var savedComment = commentRepository.save(comment);
        return new CommentDto(savedComment);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findAll() {
        return commentRepository.findAllDto();
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findAllByBookId(String bookId) {
        return commentRepository.findAllDtoByBookId(bookId);
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }
}
