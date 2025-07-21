package com.demo.advanced.exception;

public class RateLimitException extends RuntimeException {

    public static final String RATE_LIMIT = "Rate limit of %s exceeded for account %s";

    public RateLimitException(final String msgError) {
        super(msgError);
    }

}
