package com.test.warehouse.service.exception;

public class ServiceException extends Exception {

    public ServiceException(String msg) {
        super(msg);
    }

    public ServiceException (String msg, Throwable e) {
        super(msg, e);
    }
}
