package ru.otus.spring.hw.config;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import ru.otus.spring.hw.model.Coordinate;
import ru.otus.spring.hw.model.Description;

@MessagingGateway
public interface MessageGateway {

    @Gateway(requestChannel = "coordinateChannel", replyChannel = "descriptionChannel")
    Description process(Coordinate coordinate);
}
