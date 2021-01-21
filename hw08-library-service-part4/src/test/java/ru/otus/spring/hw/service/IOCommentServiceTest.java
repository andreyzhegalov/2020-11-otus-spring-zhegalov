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
        final var bookId = "1";
        final var comment1 = new CommentDto("comment", bookId);
        final var comment2 = new CommentDto("comment", bookId);

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
