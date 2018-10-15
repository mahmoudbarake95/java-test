package com.h2rd.refactoring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    private static final long serialVersionUID = -669951263598456199L;

    public BadRequestException() {}

    public BadRequestException(String message){
        super(message);
    }

}
