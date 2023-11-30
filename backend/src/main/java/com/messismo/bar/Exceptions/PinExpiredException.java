package com.messismo.bar.Exceptions;

public class PinExpiredException extends Exception {
    public PinExpiredException() {
        super();
    }

    public PinExpiredException(String message) {
        super(message);
    }

    public PinExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

}
