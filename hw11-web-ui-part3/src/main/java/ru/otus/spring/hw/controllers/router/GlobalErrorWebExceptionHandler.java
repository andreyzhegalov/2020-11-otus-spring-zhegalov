package ru.otus.spring.hw.controllers.router;

import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.web.WebProperties.Resources;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@Order(-2)
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

    public GlobalErrorWebExceptionHandler(GlobalErrorAttributes globalErrorAttributes,
            ApplicationContext applicationContext, ServerCodecConfigurer serverCodecConfigurer) {
        super(globalErrorAttributes, new Resources(), applicationContext);
        super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private @NotNull Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        final var error = getError(request);
        final Map<String, Object> errorPropertiesMap = getErrorAttributes(request, ErrorAttributeOptions.defaults());
        if (HttpStatus.valueOf((Integer) errorPropertiesMap.get("status")).is5xxServerError()) {
            log.error("internal server error: " + error.getMessage());
            errorPropertiesMap.put("status", HttpStatus.BAD_REQUEST.value());
        }
        if (error instanceof CustomRouterException) {
            errorPropertiesMap.put("errors", error.getMessage());
        }
        final var status = (int) errorPropertiesMap.get("status");
        return ServerResponse.status(status).contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(errorPropertiesMap));
    }
}
