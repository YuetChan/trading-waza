package com.tycorp.tb.exception;

public class DomainEntityNotFoundException extends RuntimeException {

    public DomainEntityNotFoundException(String message) {
        super(message);
    }

}