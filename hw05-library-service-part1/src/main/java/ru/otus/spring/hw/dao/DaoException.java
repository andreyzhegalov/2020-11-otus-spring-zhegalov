package ru.otus.spring.hw.dao;

public class DaoException extends RuntimeException {

    private static final long serialVersionUID = -7040231895654165329L;

    public DaoException(String message){
        super(message);
    }

}
