package ru.otus.spring.hw.service;

import org.springframework.stereotype.Service;

import ru.otus.spring.hw.dao.QuestionDao;
import ru.otus.spring.hw.domain.Report;
import ru.otus.spring.hw.domain.Student;
import ru.otus.spring.hw.service.front.FrontService;

@Service
public class QuizServiceImpl implements QuizService {
    private final FrontService frontService;
    private final QuestionDao questionDao;

    public QuizServiceImpl(QuestionDao questionDao, FrontService frontService) {
        this.questionDao = questionDao;
        this.frontService = frontService;
    }

    @Override
    public Report startTesting(Student student) {
        final var report = new Report(student);
        final var questionList = questionDao.getAllQuestion();
        questionList.forEach(q -> {
            final var answer = frontService.getAnswer(q);
            report.addAnswer(q, answer);
        });
        return report;
    }

    @Override
    public void printAllQuestion() {
        final var questionList = questionDao.getAllQuestion();
        questionList.forEach(frontService::getAnswer);
    }
}
