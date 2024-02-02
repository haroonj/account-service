package com.digitinary.accountservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Slf4j
@ControllerAdvice
public class AccountExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Object> handleAccountNotFoundException(AccountNotFoundException ex) {
        log.error("AccountNotFoundException {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidAccountIdException.class)
    public ResponseEntity<Object> handleInvalidAccountIdException(InvalidAccountIdException ex) {
        log.error("InvalidAccountIdException {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxAccountsReachedException.class)
    public ResponseEntity<Object> handleMaxAccountsReachedException(MaxAccountsReachedException ex) {
        log.error("MaxAccountsReachedException {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidAccountTypeException.class)
    public ResponseEntity<Object> handleInvalidAccountTypeException(InvalidAccountTypeException ex) {
        log.error("InvalidAccountTypeException {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex) {
        log.error("Exception {}", ex.getMessage());
        return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
