package com.ecommerce.demo.exceptions;

public class ProductNonExistException extends IllegalArgumentException {
    public ProductNonExistException(String msg) {
        super(msg);
    }
}
