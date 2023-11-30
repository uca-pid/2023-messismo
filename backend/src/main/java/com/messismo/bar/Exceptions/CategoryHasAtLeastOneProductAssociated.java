package com.messismo.bar.Exceptions;

public class CategoryHasAtLeastOneProductAssociated extends Exception{
    public CategoryHasAtLeastOneProductAssociated() {
        super();
    }

    public CategoryHasAtLeastOneProductAssociated(String message) {
        super(message);
    }

    public CategoryHasAtLeastOneProductAssociated(String message, Throwable cause) {
        super(message, cause);
    }
}
