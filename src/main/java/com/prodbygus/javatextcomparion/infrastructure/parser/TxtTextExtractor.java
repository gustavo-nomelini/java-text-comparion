package com.prodbygus.javatextcomparion.infrastructure.parser;

import com.prodbygus.javatextcomparion.shared.exception.FileParsingException;

/**
 * Extractor for plain text files.
 */
public class TxtTextExtractor implements TextExtractor {

    @Override
    public String extractText(byte[] fileContent) throws FileParsingException {
        try {
            return new String(fileContent, "UTF-8").trim();
        } catch (Exception e) {
            throw new FileParsingException("Failed to extract text from TXT file", e);
        }
    }

    @Override
    public boolean supports(String extension) {
        return extension != null && extension.equalsIgnoreCase("txt");
    }
}

