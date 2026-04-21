package com.prodbygus.javatextcomparion.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Document entity representing an uploaded text file.
 * Stores both original content and normalized content for comparison purposes.
 */
@Entity
@Table(name = "documents", indexes = {
        @Index(name = "idx_file_type", columnList = "file_type"),
        @Index(name = "idx_uploaded_at", columnList = "uploaded_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originalFileName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FileType fileType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String normalizedContent;

    @Column(nullable = false)
    private LocalDateTime uploadedAt;

    @Version
    private Long version;

    /**
     * Validate input parameters.
     *
     * @throws IllegalArgumentException if invalid
     */
    public void validate() {
        if (originalFileName == null || originalFileName.trim().isEmpty()) {
            throw new IllegalArgumentException("Original file name cannot be empty");
        }
        if (fileType == null) {
            throw new IllegalArgumentException("File type must be specified");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be empty");
        }
    }

    /**
     * Update normalized content for this document.
     *
     * @param normalizedContent the normalized text
     */
    public void setNormalizedContent(String normalizedContent) {
        this.normalizedContent = normalizedContent;
    }

}

