package com.demo.advanced.controller;

import org.springframework.http.HttpStatus;

public record ErrorResponse(String message, HttpStatus statusCode) {}
