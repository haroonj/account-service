package com.digitinary.accountservice.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountExceptionHandlerTest {

    @Test
    void handleAccountNotFoundException() {
        Long accountId = 1234567890L;
        AccountExceptionHandler exceptionHandler = new AccountExceptionHandler();
        AccountNotFoundException exception = new AccountNotFoundException(accountId);

        ResponseEntity<Object> response = exceptionHandler.handleAccountNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Account not found for this id :: " + accountId, response.getBody());
    }

    @Test
    void handleGlobalException() {
        
        AccountExceptionHandler exceptionHandler = new AccountExceptionHandler();
        Exception exception = new Exception("Internal server error");

        ResponseEntity<Object> response = exceptionHandler.handleGlobalException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred: Internal server error", response.getBody());
    }
}