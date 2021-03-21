package ru.otus.spring.hw.service;

import org.springframework.stereotype.Service;

import ru.otus.spring.hw.model.Address;
import ru.otus.spring.hw.model.Coordinate;

@Service
public class AddressService {
    public Address getAddress(Coordinate coordinate) {
        return new Address();
    }
}
