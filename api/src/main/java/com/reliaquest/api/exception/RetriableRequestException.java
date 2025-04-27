package com.reliaquest.api.exception;

public class RetriableRequestException extends RuntimeException {

    public RetriableRequestException(String message) {
        super(message);
    }

    public RetriableRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
