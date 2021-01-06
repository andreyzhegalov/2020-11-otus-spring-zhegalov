package ru.otus.spring.hw.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ru.otus.spring.hw.dto.CommentDto;
import ru.otus.spring.hw.model.Book;
import ru.otus.spring.hw.model.Comment;

@SpringBootTest
public class IOCommentServiceTest {
    @Import(IOCommentService.class)
    @Configuration
    public static class IOGenreServiceTestInner {
    }

    @Autowired
    private IOCommentService ioCommentService;

    @MockBean
    private IOService ioService;

    @Test
    void printCommentShouldPrintViaIOService() {
        final var comment1 = new Comment(1L, "comment", new Book());
        final var comment2 = new Comment(2L, "comment", new Book());

        ioCommentService.print(List.of(comment1, comment2));
        then(ioService).should(atLeastOnce()).print(anyString());
        then(ioService).should(never()).read();
    }

    @Test
    void shouldReadCommentFromIOService() {
        given(ioService.read()).willReturn("comment text");
        given(ioService.read()).willReturn("1");

        assertThat(ioCommentService.getComment()).isInstanceOf(CommentDto.class);

        then(ioService).should(atLeastOnce()).print(anyString());
        then(ioService).should(atLeastOnce()).read();
    }

}
