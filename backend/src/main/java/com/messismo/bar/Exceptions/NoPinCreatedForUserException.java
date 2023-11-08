package com.messismo.bar.Exceptions;

public class NoPinCreatedForUserException extends Exception{
    public NoPinCreatedForUserException() {
        super();
    }

    public NoPinCreatedForUserException(String message) {
        super(message);
    }

    public NoPinCreatedForUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
