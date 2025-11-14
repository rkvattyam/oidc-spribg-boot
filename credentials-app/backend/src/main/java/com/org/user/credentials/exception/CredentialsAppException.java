package com.org.user.credentials.exception;

import java.io.Serializable;

public class CredentialsAppException extends RuntimeException implements Serializable {

    public CredentialsAppException(final String message) {
        super(message);
    }

    public CredentialsAppException(final String message, final Exception ex) {
        super(message, ex);
    }
}
