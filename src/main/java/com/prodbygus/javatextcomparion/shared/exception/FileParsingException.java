package com.prodbygus.javatextcomparion.shared.exception;

/**
 * Exception thrown when file parsing fails.
 */
public class FileParsingException extends TextComparisonException {

    public FileParsingException(String message) {
        super(message);
    }

    public FileParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}

