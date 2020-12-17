package ru.otus.spring.hw.event.events;

import lombok.Getter;
import ru.otus.spring.hw.domain.Report;

public class PrintReportEvent extends AbstractCustomEvent {
    private static final long serialVersionUID = 8628415201996608167L;
    @Getter
    private final Report report;

    public PrintReportEvent(Object source, Report report) {
        super(source);
        this.report = report;
    }

}
