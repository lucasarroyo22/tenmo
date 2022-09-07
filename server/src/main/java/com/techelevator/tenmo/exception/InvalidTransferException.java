package com.techelevator.tenmo.exception;

public class InvalidTransferException extends RuntimeException {
    public InvalidTransferException() {
        super();
    }

    public InvalidTransferException(String message) {
        super(message);
    }

    public InvalidTransferException(String message, Exception cause) {
        super(message, cause);
    }
}
