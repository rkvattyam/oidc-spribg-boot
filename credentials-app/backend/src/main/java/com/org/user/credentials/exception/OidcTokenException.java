package com.org.user.credentials.exception;

public class OidcTokenException extends RuntimeException {

    private final int statusCode;

    public OidcTokenException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}

