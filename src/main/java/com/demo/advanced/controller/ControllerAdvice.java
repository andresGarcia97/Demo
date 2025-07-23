package com.demo.advanced.controller;

import com.demo.advanced.dto.response.ErrorResponse;
import com.demo.advanced.exception.AccountBankException;
import com.demo.advanced.exception.ClientException;
import com.demo.advanced.exception.RateLimitException;
import com.demo.advanced.exception.TransactionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvice {

    @ExceptionHandler({
            AccountBankException.class,
            ClientException.class,
            TransactionException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<ErrorResponse> handleDomainException(Exception exception) {
        log.info("Domain Error: {}", exception.getClass());
        log.error("Domain Error: ", exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler({ RuntimeException.class, Exception.class })
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception exception) {
        log.info("General Error: {}", exception.getClass());
        log.warn("General Error: {}", exception.getMessage());
        log.error("General Error: ", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(RateLimitException.class)
    public ResponseEntity<ErrorResponse> handleRateLimit(Exception exception) {
        log.warn("Rate limit Error: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ErrorResponse(exception.getMessage(), HttpStatus.SERVICE_UNAVAILABLE));
    }

    @ExceptionHandler({ NoResourceFoundException.class, HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<ErrorResponse> handleBadResources(Exception exception) {
        log.warn("Error resource: {}", exception.getMessage());
        log.error("Error resource: ", exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND));
    }

}
