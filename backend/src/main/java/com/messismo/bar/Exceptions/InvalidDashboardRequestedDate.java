package com.messismo.bar.Exceptions;

public class InvalidDashboardRequestedDate extends Exception{
    public InvalidDashboardRequestedDate() {
        super();
    }

    public InvalidDashboardRequestedDate(String message) {
        super(message);
    }

    public InvalidDashboardRequestedDate(String message, Throwable cause) {
        super(message, cause);
    }
}
