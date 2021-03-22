package ru.otus.spring.hw.service;

import org.springframework.stereotype.Service;

import ru.otus.spring.hw.model.Address;
import ru.otus.spring.hw.model.Description;

@Service
public class DescriptionService {

    public Description getDescription(Address coordinate) {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        return new Description();
    }

}
