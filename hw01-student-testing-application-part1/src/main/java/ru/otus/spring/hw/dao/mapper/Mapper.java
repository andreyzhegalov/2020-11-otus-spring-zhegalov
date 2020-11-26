package ru.otus.spring.hw.dao.mapper;

public interface Mapper<I, O> {

    O convert(I data);
}

