package com.prodbygus.javatextcomparion.application.dto;

import java.time.LocalDateTime;

/**
 * DTO for document upload response.
 */
public record DocumentDto(
        Long id,
        String originalFileName,
        String fileType,
        LocalDateTime uploadedAt
) {
}

