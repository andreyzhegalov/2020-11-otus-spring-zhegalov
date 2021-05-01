package ru.otus.spring.hw.service;

import java.util.Collections;
import java.util.List;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import org.assertj.core.util.Lists;
import org.springframework.cloud.client.loadbalancer.reactive.RetryableLoadBalancerExchangeFilterFunction;
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

    @Transactional(readOnly = true)
    @Override
    @HystrixCommand(fallbackMethod = "findAllFallbackHandler")
    public List<CommentDto> findAll() {
        return commentRepository.findAllDto();
    }

    public List<CommentDto> findAllFallbackHandler() {
        return Lists.emptyList();
    }

    @Transactional(readOnly = true)
    @HystrixCommand(fallbackMethod = "findAllByBookIdFallbackHandler")
    @Override
    public List<CommentDto> findAllByBookId(String bookId) {
        return commentRepository.findAllDtoByBookId(bookId);
    }

    @SuppressWarnings("unused")
    private List<CommentDto> findAllByBookIdFallbackHandler(String bookId) {
        return Collections.emptyList();
    }

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

    @Transactional
    @HystrixCommand(fallbackMethod = "deleteCommentFallbackHandler")
    @Override
    public boolean deleteById(String id) {
        commentRepository.deleteById(id);
        return true;
    }

    @SuppressWarnings("unused")
    private boolean deleteCommentFallbackHandler(String id) {
        return false;
    }
}
