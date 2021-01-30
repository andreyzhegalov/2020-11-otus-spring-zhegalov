package ru.otus.spring.hw.repositories;

import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;

@DataMongoTest
@ComponentScan({ "ru.otus.spring.hw.repositories" })
public abstract class AbstractRepositoryTest {

}
