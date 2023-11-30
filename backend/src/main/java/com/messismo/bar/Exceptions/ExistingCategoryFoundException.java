package com.messismo.bar.Exceptions;

public class ExistingCategoryFoundException extends Exception{
    public ExistingCategoryFoundException() {
        super();
    }

    public ExistingCategoryFoundException(String message) {
        super(message);
    }

    public ExistingCategoryFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
