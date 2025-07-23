package com.demo.advanced.exception;

public class RateLimitException extends RuntimeException {

    public static final String RATE_LIMIT = "Rate limit exceeded";

    public RateLimitException(final String msgError) {
        super(msgError);
    }

}
