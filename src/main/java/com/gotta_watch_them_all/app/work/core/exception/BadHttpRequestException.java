package com.gotta_watch_them_all.app.work.core.exception;

public class BadHttpRequestException extends RuntimeException {
    public BadHttpRequestException(String message) {
        super(message);
    }
}
