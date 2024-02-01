package com.digitinary.accountservice.exception;

public class MaxAccountsReachedException extends RuntimeException {
    public MaxAccountsReachedException(String message) {
        super(message);
    }
}