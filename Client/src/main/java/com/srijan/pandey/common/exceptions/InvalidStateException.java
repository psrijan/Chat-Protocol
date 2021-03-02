package com.srijan.pandey.common.exceptions;

/**
 * If the received message and the state of the server/client does
 * not match. Then send an invalid state exception.
 */
public class InvalidStateException extends RuntimeException{

    public InvalidStateException(String message) {
        super(message);
    }

    public InvalidStateException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public String getLocalizedMessage() {
        return super.getLocalizedMessage();
    }

    @Override
    public synchronized Throwable getCause() {
        return super.getCause();
    }
}
