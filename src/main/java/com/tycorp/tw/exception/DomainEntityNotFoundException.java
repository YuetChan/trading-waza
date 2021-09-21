package com.tycorp.tw.exception;

public class DomainEntityNotFoundException extends RuntimeException {

    public DomainEntityNotFoundException(String message) {
        super(message);
    }

}