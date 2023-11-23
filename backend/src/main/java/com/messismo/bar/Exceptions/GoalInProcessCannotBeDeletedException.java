package com.messismo.bar.Exceptions;

public class GoalInProcessCannotBeDeletedException extends Exception{
    public GoalInProcessCannotBeDeletedException() {
        super();
    }

    public GoalInProcessCannotBeDeletedException(String message) {
        super(message);
    }

    public GoalInProcessCannotBeDeletedException(String message, Throwable cause) {
        super(message, cause);
    }
}
