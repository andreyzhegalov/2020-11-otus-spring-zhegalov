package ru.otus.spring.hw.event.events;

import ru.otus.spring.hw.domain.Report;

public class ReportEvent extends AbstractCustomEvent {
    private static final long serialVersionUID = -7308237831439770594L;

    public ReportEvent(Object source, Report payload) {
        super(source, payload);
    }

}
