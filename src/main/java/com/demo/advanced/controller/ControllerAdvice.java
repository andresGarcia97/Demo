package com.demo.advanced.controller;

import com.demo.advanced.domain.accountbank.AccountBankException;
import com.demo.advanced.domain.client.ClientException;
import com.demo.advanced.domain.transaction.TransactionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvice {

    @ExceptionHandler( { AccountBankException.class, ClientException.class, TransactionException.class } )
    public ResponseEntity<ErrorResponse> handleDomainException(Exception exception) {
        log.error("Domain Error: ", exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler( { RuntimeException.class, Exception.class })
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception exception) {
        log.error("General Error: ", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
    }

}
