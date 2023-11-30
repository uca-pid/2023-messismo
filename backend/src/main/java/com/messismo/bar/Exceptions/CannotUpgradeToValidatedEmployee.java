package com.messismo.bar.Exceptions;

public class CannotUpgradeToValidatedEmployee extends Exception{
    public CannotUpgradeToValidatedEmployee() {
        super();
    }

    public CannotUpgradeToValidatedEmployee(String message) {
        super(message);
    }

    public CannotUpgradeToValidatedEmployee(String message, Throwable cause) {
        super(message, cause);
    }
}
