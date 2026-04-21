package com.prodbygus.javatextcomparion.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Comparison entity representing the result of comparing two documents.
 * Stores similarity metrics and the final correlation index.
 */
@Entity
@Table(name = "comparisons", indexes = {
        @Index(name = "idx_created_at", columnList = "created_at"),
        @Index(name = "idx_document_a", columnList = "document_a_id"),
        @Index(name = "idx_document_b", columnList = "document_b_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comparison {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "document_a_id", nullable = false)
    private Document documentA;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "document_b_id", nullable = false)
    private Document documentB;

    @Column(nullable = false)
    private Double cosineSimilarity;

    @Column(nullable = false)
    private Double jaccardSimilarity;

    @Column(nullable = false)
    private Double correlationIndex;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "comparison", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<MatchSegment> matchSegments = new ArrayList<>();

    @Version
    private Long version;

    /**
     * Validate input parameters.
     *
     * @throws IllegalArgumentException if invalid
     */
    public void validate() {
        if (documentA == null) {
            throw new IllegalArgumentException("Document A must be specified");
        }
        if (documentB == null) {
            throw new IllegalArgumentException("Document B must be specified");
        }
        if (documentA.getId().equals(documentB.getId())) {
            throw new IllegalArgumentException("Cannot compare a document with itself");
        }
        if (cosineSimilarity == null || cosineSimilarity < 0 || cosineSimilarity > 1) {
            throw new IllegalArgumentException("Cosine similarity must be between 0 and 1");
        }
        if (jaccardSimilarity == null || jaccardSimilarity < 0 || jaccardSimilarity > 1) {
            throw new IllegalArgumentException("Jaccard similarity must be between 0 and 1");
        }
        if (correlationIndex == null || correlationIndex < 0 || correlationIndex > 1) {
            throw new IllegalArgumentException("Correlation index must be between 0 and 1");
        }
    }

    /**
     * Add a match segment to this comparison.
     *
     * @param segment the segment to add
     */
    public void addMatchSegment(MatchSegment segment) {
        matchSegments.add(segment);
        segment.setComparison(this);
    }

}

