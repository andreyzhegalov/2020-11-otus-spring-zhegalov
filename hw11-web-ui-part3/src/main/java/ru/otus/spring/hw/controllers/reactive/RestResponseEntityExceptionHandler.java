package ru.otus.spring.hw.controllers.reactive;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import ru.otus.spring.hw.repositories.RepositoryException;
import ru.otus.spring.hw.service.ServiceException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler(value = { RepositoryException.class, ServiceException.class })
    public ResponseEntity<Object> repositoryExceptionHandler(Throwable ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", HttpStatus.BAD_REQUEST);
        body.put("errors", ex.getMessage());
        final var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
    }

    // @Override
    // protected ResponseEntity<Object>
    // handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
    // HttpHeaders headers, HttpStatus status, WebRequest request) {
    //
    // Map<String, Object> body = new LinkedHashMap<>();
    // body.put("timestamp", new Da
    // e());
    // .put("status", status.value());
    //
    // List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(x
    // -> x.getDefaultMessage())
    // .collect(Collectors.toList());
    // // body.put("errors", errors);
    //
    // // ew ResponseEntity<>(body, headers, status);
    //
    //
}
