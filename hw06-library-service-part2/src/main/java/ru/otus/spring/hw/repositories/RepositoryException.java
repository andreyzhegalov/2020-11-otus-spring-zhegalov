package ru.otus.spring.hw.repositories;

public class RepositoryException extends RuntimeException {

    private static final long serialVersionUID = 8727055428394260086L;

    public RepositoryException(Throwable other) {
        super(other);
    }
}
