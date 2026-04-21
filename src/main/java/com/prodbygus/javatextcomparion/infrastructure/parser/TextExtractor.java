package com.prodbygus.javatextcomparion.infrastructure.parser;

import com.prodbygus.javatextcomparion.shared.exception.FileParsingException;

/**
 * Interface for extracting text from files of different formats.
 */
public interface TextExtractor {

    /**
     * Extract text content from file bytes.
     *
     * @param fileContent the file bytes
     * @return extracted text
     * @throws FileParsingException if extraction fails
     */
    String extractText(byte[] fileContent) throws FileParsingException;

    /**
     * Check if this extractor supports the given file extension.
     *
     * @param extension the file extension (e.g., "txt", "pdf", "docx")
     * @return true if supported
     */
    boolean supports(String extension);
}

