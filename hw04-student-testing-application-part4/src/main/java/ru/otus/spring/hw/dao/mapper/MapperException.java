package ru.otus.spring.hw.dao.mapper;

public class MapperException extends RuntimeException {

    private static final long serialVersionUID = 5970479991653490332L;

    MapperException(String msg) {
        super(msg);
    }
}
