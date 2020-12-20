package ru.otus.spring.hw.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.model.Book;

@RequiredArgsConstructor
@Component
public class BookDaoJdbs implements BookDao {

    @Override
    public long insertOrUpdate(Book book) {
        return 0L;
    }

    @Override
	public List<Book> getAll() {
        return new ArrayList<>();
	}

}
