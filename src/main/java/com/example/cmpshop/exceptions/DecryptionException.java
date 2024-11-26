package com.example.cmpshop.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.security.GeneralSecurityException;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class DecryptionException extends RuntimeException {
    public DecryptionException (String message, GeneralSecurityException e){
        super(message);
    }
}
