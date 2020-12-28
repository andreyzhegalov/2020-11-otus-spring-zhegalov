package ru.otus.spring.hw.repositories;

public class RepositoryException extends RuntimeException {

    public RepositoryException() {

    }

    public RepositoryException(String msg) {
        super(msg);
    }

}
