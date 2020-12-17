package ru.otus.spring.hw.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.event.events.PrintReportEvent;
import ru.otus.spring.hw.service.front.ReportService;

@RequiredArgsConstructor
@Component
public class PrintReportEventHandler implements EventHandler<PrintReportEvent>{
    private final ReportService reportService;

    @EventListener
	@Override
	public void handle(PrintReportEvent event) {
        final var report = event.getReport();
        reportService.printResult(report);
	}
}
