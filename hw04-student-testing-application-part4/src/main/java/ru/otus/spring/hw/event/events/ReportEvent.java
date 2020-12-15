package ru.otus.spring.hw.event.events;

import ru.otus.spring.hw.domain.Report;

public class ReportEvent extends CustomEvent {
    private static final long serialVersionUID = 1L;

    public ReportEvent(Object source, Report payload) {
        super(source, payload);
    }

}
