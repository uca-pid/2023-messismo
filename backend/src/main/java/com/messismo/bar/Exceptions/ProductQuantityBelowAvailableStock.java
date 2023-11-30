package com.messismo.bar.Exceptions;

public class ProductQuantityBelowAvailableStock extends Exception{
    public ProductQuantityBelowAvailableStock() {
        super();
    }

    public ProductQuantityBelowAvailableStock(String message) {
        super(message);
    }

    public ProductQuantityBelowAvailableStock(String message, Throwable cause) {
        super(message, cause);
    }
}
