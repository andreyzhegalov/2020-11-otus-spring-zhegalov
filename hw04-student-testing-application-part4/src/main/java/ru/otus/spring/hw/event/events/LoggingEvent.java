package ru.otus.spring.hw.event.events;

public class LoggingEvent extends CustomEvent {

	private static final long serialVersionUID = 1L;

	public LoggingEvent(Object source) {
        super(source, null);
    }

}
