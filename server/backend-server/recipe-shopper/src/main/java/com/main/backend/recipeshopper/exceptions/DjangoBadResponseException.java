package com.main.backend.recipeshopper.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class DjangoBadResponseException extends RuntimeException {
    public DjangoBadResponseException(String msg) {
        super(msg);
    }
}
