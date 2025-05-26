package com.app.loans.exception;

public class LoanAlreadyExistsException extends RuntimeException {

    public LoanAlreadyExistsException(String message) {
        super(message);
    }
}