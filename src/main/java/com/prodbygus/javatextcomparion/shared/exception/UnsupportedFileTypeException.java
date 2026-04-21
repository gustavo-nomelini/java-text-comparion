package com.prodbygus.javatextcomparion.shared.exception;

/**
 * Exception thrown when file format is not supported.
 */
public class UnsupportedFileTypeException extends TextComparisonException {

    public UnsupportedFileTypeException(String message) {
        super(message);
    }

    public UnsupportedFileTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}

