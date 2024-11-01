package com.spring.commonlib.exceptions;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class FieldException extends RuntimeException {

    protected final String code;
    protected final String field;

    public FieldException(String message, String code, String field) {
        super(message);
        log.info("message :", message);
        this.code = code;
        this.field = field;
    }
}
