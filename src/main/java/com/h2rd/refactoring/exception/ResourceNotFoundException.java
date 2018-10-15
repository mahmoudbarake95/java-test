package com.h2rd.refactoring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -5937304209517503460L;

    public ResourceNotFoundException() {}

    public ResourceNotFoundException(String message){
        super(message);
    }

}