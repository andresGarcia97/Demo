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
    public ResponseEntity<ErrorResponse> handleNotValidArgumentException(Exception exception) {
        log.error("ERROR: ", exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST));
    }

}
