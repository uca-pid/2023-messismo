package com.messismo.bar.Exceptions;

public class EndingDateMustBeAfterStartingDateException extends Exception{
    public EndingDateMustBeAfterStartingDateException() {
        super();
    }

    public EndingDateMustBeAfterStartingDateException(String message) {
        super(message);
    }

    public EndingDateMustBeAfterStartingDateException(String message, Throwable cause) {
        super(message, cause);
    }
}
