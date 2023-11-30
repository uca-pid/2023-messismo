package com.messismo.bar.Exceptions;

public class GoalExpiredCannotBeModifiedException extends Exception {
    public GoalExpiredCannotBeModifiedException() {
        super();
    }

    public GoalExpiredCannotBeModifiedException(String message) {
        super(message);
    }

    public GoalExpiredCannotBeModifiedException(String message, Throwable cause) {
        super(message, cause);
    }
}
