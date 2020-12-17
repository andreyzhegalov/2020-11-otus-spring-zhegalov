package ru.otus.spring.hw.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.domain.Report;
import ru.otus.spring.hw.event.events.PrintReportEvent;
import ru.otus.spring.hw.service.front.ReportService;

@RequiredArgsConstructor
@Component
public class PrintReportEventHandler {
    private final ReportService reportService;

    @EventListener
    public void onPrintReportEvent(PrintReportEvent event) {
        final var report = (Report) event.getPayload();
        reportService.printResult(report);
    }
}
