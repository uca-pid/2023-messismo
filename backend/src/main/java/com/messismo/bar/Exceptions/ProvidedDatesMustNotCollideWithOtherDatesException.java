package com.messismo.bar.Exceptions;

public class ProvidedDatesMustNotCollideWithOtherDatesException extends Exception{
    public ProvidedDatesMustNotCollideWithOtherDatesException() {
        super();
    }

    public ProvidedDatesMustNotCollideWithOtherDatesException(String message) {
        super(message);
    }

    public ProvidedDatesMustNotCollideWithOtherDatesException(String message, Throwable cause) {
        super(message, cause);
    }
}
