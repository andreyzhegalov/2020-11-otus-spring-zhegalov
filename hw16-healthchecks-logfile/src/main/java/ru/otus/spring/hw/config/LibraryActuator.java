package ru.otus.spring.hw.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.hw.repositories.BookRepository;

@RequiredArgsConstructor
@Component
@Endpoint(id = "libraryEndpoint")
public class LibraryActuator {
    private final BookRepository bookRepository;

    @ReadOperation
    public ObjectNode books() throws JsonProcessingException {
        final var libraryEndpoint = new ObjectMapper().createObjectNode();
        libraryEndpoint.put("bookCnt", bookRepository.count());
        return libraryEndpoint;
    }
}
