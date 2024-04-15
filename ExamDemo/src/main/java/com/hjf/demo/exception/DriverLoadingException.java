package com.hjf.demo.exception;

public class DriverLoadingException extends RuntimeException {
    public DriverLoadingException(String message) {
        super(message);
    }

    public DriverLoadingException(String message, Throwable cause) {
        super(message, cause);
    }
}
