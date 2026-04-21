package com.prodbygus.javatextcomparion.application.dto;

/**
 * Request DTO for comparing two documents.
 */
public record ComparisonRequest(
        Long documentAId,
        Long documentBId
) {
    /**
     * Validate the request.
     *
     * @throws IllegalArgumentException if invalid
     */
    public void validate() {
        if (documentAId == null) {
            throw new IllegalArgumentException("Document A ID must be specified");
        }
        if (documentBId == null) {
            throw new IllegalArgumentException("Document B ID must be specified");
        }
        if (documentAId.equals(documentBId)) {
            throw new IllegalArgumentException("Cannot compare a document with itself");
        }
    }
}

