package com.demo.advanced.dto.response;

import org.springframework.http.HttpStatus;

public record ErrorResponse(String message, HttpStatus statusCode) {}
