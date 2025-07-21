package com.demo.advanced.controller.error;

import org.springframework.http.HttpStatus;

public record ErrorResponse(String message, HttpStatus statusCode) {}
