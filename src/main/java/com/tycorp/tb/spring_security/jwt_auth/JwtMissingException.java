package com.tycorp.tb.spring_security.jwt_auth;

import org.springframework.security.core.AuthenticationException;

public class JwtMissingException extends AuthenticationException {

    public JwtMissingException(String msg) {
        super(msg);
    }

}
