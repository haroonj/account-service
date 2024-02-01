package com.digitinary.accountservice.exception;

public class InvalidAccountIdException extends RuntimeException {
    public InvalidAccountIdException(String message) {
        super(message);
    }
}