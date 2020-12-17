package ru.otus.spring.hw.event.events;

import lombok.Getter;
import ru.otus.spring.hw.domain.Report;

public class ReportEvent extends AbstractCustomEvent {
    private static final long serialVersionUID = -7308237831439770594L;
    @Getter
    private final Report report;

    public ReportEvent(Object source, Report payload) {
        super(source);
        this.report = payload;
    }

}
