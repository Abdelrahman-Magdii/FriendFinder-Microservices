package com.spring.usermanagement.exceptions;

import lombok.Getter;
import org.springframework.security.authentication.BadCredentialsException;

@Getter
public class BadAuthException extends BadCredentialsException {
    protected final String code;

    public BadAuthException(String message, String code) {
        super(message);
        this.code = code;
    }
}
