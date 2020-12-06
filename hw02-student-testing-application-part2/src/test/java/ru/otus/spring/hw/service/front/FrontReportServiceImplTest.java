package ru.otus.spring.hw.service.front;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.otus.spring.hw.domain.Report;
import ru.otus.spring.hw.service.IOService;

@ExtendWith(MockitoExtension.class)
class FrontReportServiceImplTest {

    @Mock
    private IOService ioService;

    @Test
    void printResult() {
        final var report = new Report("text");
        new FrontReportServiceImpl(ioService).printResult(report);
        then(ioService).should().print(anyString());
    }
}
