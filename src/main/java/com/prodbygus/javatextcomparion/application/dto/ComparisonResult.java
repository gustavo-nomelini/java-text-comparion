package com.prodbygus.javatextcomparion.application.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO with comparison results.
 */
public record ComparisonResult(
        Long id,
        Long documentAId,
        Long documentBId,
        String documentAName,
        String documentBName,
        Double cosineSimilarity,
        Double jaccardSimilarity,
        Double correlationIndex,
        String summary,
        LocalDateTime createdAt,
        List<MatchSegmentDto> matchSegments
) {
}

