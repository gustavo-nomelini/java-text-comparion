package com.prodbygus.javatextcomparion.shared.exception;

/**
 * Base exception for text comparison system.
 */
public class TextComparisonException extends RuntimeException {

    public TextComparisonException(String message) {
        super(message);
    }

    public TextComparisonException(String message, Throwable cause) {
        super(message, cause);
    }
}

