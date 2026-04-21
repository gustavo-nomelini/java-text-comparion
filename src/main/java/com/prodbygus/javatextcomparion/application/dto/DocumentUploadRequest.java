package com.prodbygus.javatextcomparion.application.dto;

import java.time.LocalDateTime;

/**
 * DTO for uploading a document.
 */
public record DocumentUploadRequest(
        String filename,
        String content
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
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("File content must not be empty");
        }
    }
}

