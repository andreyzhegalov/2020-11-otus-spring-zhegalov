package ru.otus.spring.hw.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import ru.otus.spring.hw.model.Address;
import ru.otus.spring.hw.model.Coordinate;

@Service
public class AddressService {
    public Optional<Address> getAddress(Coordinate coordinate) {
        return Optional.of(new Address());
    }
}
