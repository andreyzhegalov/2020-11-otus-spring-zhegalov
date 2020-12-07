package ru.otus.spring.hw.dao;

public class QuestionDaoException extends RuntimeException {

    public QuestionDaoException() {

    }

    public QuestionDaoException(Throwable cause) {
        super(cause);
    }

    public QuestionDaoException(String msg) {
        super(msg);
    }
}
