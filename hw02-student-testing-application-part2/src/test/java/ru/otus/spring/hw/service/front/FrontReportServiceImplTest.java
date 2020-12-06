package ru.otus.spring.hw.service.front;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.otus.spring.hw.controller.IOController;
import ru.otus.spring.hw.domain.Report;
import ru.otus.spring.hw.domain.Student;

@ExtendWith(MockitoExtension.class)
class FrontReportServiceImplTest {

    @Mock
    private IOController ioController;

    @Test
    void printResult() {
        final var report = new Report(new Student("ivan", "ivanov"));
        new FrontReportServiceImpl( ioController).printResult(report);
        then(ioController).should().print(anyString());
    }
}
