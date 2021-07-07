package com.test.warehouse.dao.exception;

public class InsufficientAmountInStock extends Exception {

    public InsufficientAmountInStock(Throwable e) {
        super(e);
    }

    public InsufficientAmountInStock(String msg) {
        super(msg);
    }
}
