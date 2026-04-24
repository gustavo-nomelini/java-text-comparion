package com.prodbygus.javatextcomparion.application.dto;

/**
 * DTO for uploading a document.
 */
public record DocumentUploadRequest(
        String filename,
        byte[] fileContent
) {
    /**
     * Validate the request.
     *
     * @throws IllegalArgumentException if invalid
     */
    public void validate() {
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("Filename must be specified");
        }
        if (fileContent == null || fileContent.length == 0) {
            throw new IllegalArgumentException("File content must not be empty");
        }
    }
}

