package ru.otus.spring.hw.config;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import ru.otus.spring.hw.model.Address;
import ru.otus.spring.hw.model.Coordinate;

@MessagingGateway
public interface MessageGateway {

    @Gateway(requestChannel = "coordinateChannel", replyChannel = "addressChannel")
    Address process(Coordinate coordinate);
}
