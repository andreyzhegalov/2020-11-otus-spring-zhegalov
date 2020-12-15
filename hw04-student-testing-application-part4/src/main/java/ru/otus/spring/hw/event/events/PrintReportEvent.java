package ru.otus.spring.hw.event.events;

import ru.otus.spring.hw.domain.Report;

public class PrintReportEvent extends CustomEvent {

    private static final long serialVersionUID = 1L;

    public PrintReportEvent(Object source, Report report) {
        super(source, report);
    }

}
