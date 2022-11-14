package com.example.hrautomationbackend.controller;

import com.example.hrautomationbackend.exception.AccessTokenIsNotValidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.tinkoff.ia.security.reserve.domain.reserve.NotFoundCalculationException;
import ru.tinkoff.ia.web.api.Errors;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice()
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity handleValidationError(final AccessTokenIsNotValidException e) {
        return ResponseEntity.status(401).body(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(code = NOT_FOUND)
    public Errors notFoundCalculationError(final NotFoundCalculationException e) {
        log.warn("Not found calculating exception", e);
        return new Errors(List.of(new Error("calculationId", e.getMessage())));
    }

    @ExceptionHandler
    @ResponseStatus(code = INTERNAL_SERVER_ERROR)
    public Errors runtimeError(final RuntimeException e) {
        log.error("Runtime error exception", e);
        return new Errors(List.of(new Error(null, "Внутренняя ошибка")));
    }

    public static Error toDto(ObjectError error) {
        if (error instanceof FieldError fieldError) {
            return new Error(fieldError.getField(), fieldError.getDefaultMessage());
        } else {
            return new Error(null, error.getDefaultMessage());
        }
    }
}
