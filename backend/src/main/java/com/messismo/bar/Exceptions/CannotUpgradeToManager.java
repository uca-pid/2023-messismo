package com.messismo.bar.Exceptions;

public class CannotUpgradeToManager extends Exception{
    public CannotUpgradeToManager() {
        super();
    }

    public CannotUpgradeToManager(String message) {
        super(message);
    }

    public CannotUpgradeToManager(String message, Throwable cause) {
        super(message, cause);
    }
}
