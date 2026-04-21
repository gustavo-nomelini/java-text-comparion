package com.prodbygus.javatextcomparion.application.dto;

/**
 * DTO for a matched segment between two documents.
 */
public record MatchSegmentDto(
        Long id,
        String sourceExcerpt,
        String targetExcerpt,
        Double similarityScore,
        String category
) {
}

