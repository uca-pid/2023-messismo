package com.messismo.bar.Exceptions;

public class GoalIdNotFoundException extends Exception{
    public GoalIdNotFoundException() {
        super();
    }

    public GoalIdNotFoundException(String message) {
        super(message);
    }

    public GoalIdNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
