package ru.otus.spring.hw.repositories;

import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("withMongo")
@DataMongoTest
@ComponentScan({ "ru.otus.spring.hw.repositories" })
public abstract class AbstractRepositoryTest {

}
