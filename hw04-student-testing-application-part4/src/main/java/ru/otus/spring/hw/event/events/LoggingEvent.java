package ru.otus.spring.hw.event.events;

public class LoggingEvent extends AbstractCustomEvent {
    private static final long serialVersionUID = -2880285010116233526L;

    public LoggingEvent(Object source) {
        super(source);
    }

}
