package ru.otus.spring.hw.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.domain.Student;
import ru.otus.spring.hw.event.events.AbstractCustomEvent;
import ru.otus.spring.hw.event.events.ReportEvent;
import ru.otus.spring.hw.event.events.StartQuizEvent;
import ru.otus.spring.hw.service.QuizService;

@RequiredArgsConstructor
@Component
public class StartQuizEventHandler implements EventHandler<StartQuizEvent>{
    private final EventPublisher<AbstractCustomEvent> eventPublisher;
    private final QuizService quizService;

    @EventListener
	@Override
	public void handle(StartQuizEvent event) {
        final var report = quizService.startTesting((Student) event.getPayload());
        eventPublisher.publish(new ReportEvent(this, report));
	}
}
