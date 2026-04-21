package com.prodbygus.javatextcomparion.shared.exception;

/**
 * Exception thrown when validation fails.
 */
public class ValidationException extends TextComparisonException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}

