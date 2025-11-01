package com.au.sportsbet.ticket.system.api.exception;

/**
 * Exception class for Ticket booking exception.
 */
public class TransactionException extends RuntimeException {

    public TransactionException(String errorMessage) {
        super(errorMessage);
    }

    public TransactionException(String errorMessage, Throwable e) {
        super(errorMessage, e);
    }
}
