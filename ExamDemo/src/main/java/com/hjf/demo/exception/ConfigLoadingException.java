package com.hjf.demo.exception;

public class ConfigLoadingException extends RuntimeException {
    public ConfigLoadingException(String message) {
        super(message);
    }

    public ConfigLoadingException(String message, Throwable cause) {
        super(message, cause);
    }
}
