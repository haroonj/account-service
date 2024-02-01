package com.digitinary.accountservice.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(Long id) {
        super("Account not found for this id :: " + id);
    }
}
