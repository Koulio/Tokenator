package org.tokenator.opentokenizer;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Invalid PAN")
public class InvalidAccountNumberException extends RuntimeException {
    public InvalidAccountNumberException(String message) {
        super(message);
    }
}
