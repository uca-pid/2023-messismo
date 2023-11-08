package com.messismo.bar.Exceptions;

public class ExistingProductFoundException extends Exception{
    public ExistingProductFoundException() {
        super();
    }

    public ExistingProductFoundException(String message) {
        super(message);
    }

    public ExistingProductFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
