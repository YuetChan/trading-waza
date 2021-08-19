package com.tycorp.eb.exception;

public class DomainEntityNotFoundException extends RuntimeException {

    public DomainEntityNotFoundException(String message) {
        super(message);
    }

}