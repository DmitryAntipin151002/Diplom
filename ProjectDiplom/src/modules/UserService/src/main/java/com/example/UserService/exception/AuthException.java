package com.example.UserService.exception;


public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}