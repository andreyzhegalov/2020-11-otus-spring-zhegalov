package ru.otus.spring.hw.dao;

public class QuestionDaoException extends RuntimeException {

    private static final long serialVersionUID = 1708479037884004754L;

    public QuestionDaoException() {

    }

    public QuestionDaoException(Throwable cause) {
        super(cause);
    }

    public QuestionDaoException(String msg) {
        super(msg);
    }
}
