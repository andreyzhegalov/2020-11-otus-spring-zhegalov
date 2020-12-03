package ru.otus.spring.hw;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
        // final var frontService = context.getBean(FrontService.class);
        // frontService.printAllQuestion();
        context.close();
    }
}
