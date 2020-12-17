package ru.otus.spring.hw.event;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ru.otus.spring.hw.domain.Report;
import ru.otus.spring.hw.domain.Student;
import ru.otus.spring.hw.event.events.PrintReportEvent;
import ru.otus.spring.hw.service.front.ReportService;

@SpringBootTest
class PrintReportEventHandlerTest {
    @MockBean
    private ReportService reportService;

    @Autowired
    private PrintReportEventHandler eventHandler;

    @Test
    void shouldPrintReportByEvent() {
        final var student = new Student("ivan", "ivanov");
        final var report = new Report(student);

        eventHandler.onPrintReportEvent(new PrintReportEvent(this, report));

        then(reportService).should().printResult(eq(report));
    }
}
