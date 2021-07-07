package com.test.warehouse.web.handler;

import com.test.warehouse.dao.exception.InsufficientAmountInStock;
import com.test.warehouse.service.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InsufficientAmountInStock.class)
    public ResponseEntity<Object> handleInsufficientAmountException(InsufficientAmountInStock e, WebRequest request) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ServiceException.class, IOException.class})
    public ResponseEntity<Object> handleServiceException(Exception e, WebRequest request) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
