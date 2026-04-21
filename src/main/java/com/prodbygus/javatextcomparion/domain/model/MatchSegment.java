package com.prodbygus.javatextcomparion.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MatchSegment entity representing a pair of related text excerpts
 * from two documents being compared.
 */
@Entity
@Table(name = "match_segments", indexes = {
        @Index(name = "idx_comparison", columnList = "comparison_id"),
        @Index(name = "idx_category", columnList = "category")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchSegment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comparison_id", nullable = false)
    private Comparison comparison;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String sourceExcerpt;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String targetExcerpt;

    @Column(nullable = false)
    private Double similarityScore;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SegmentCategory category;

    @Version
    private Long version;

    /**
     * Validate input parameters.
     *
     * @throws IllegalArgumentException if invalid
     */
    public void validate() {
        if (comparison == null) {
            throw new IllegalArgumentException("Comparison must be specified");
        }
        if (sourceExcerpt == null || sourceExcerpt.trim().isEmpty()) {
            throw new IllegalArgumentException("Source excerpt cannot be empty");
        }
        if (targetExcerpt == null || targetExcerpt.trim().isEmpty()) {
            throw new IllegalArgumentException("Target excerpt cannot be empty");
        }
        if (similarityScore == null || similarityScore < 0 || similarityScore > 1) {
            throw new IllegalArgumentException("Similarity score must be between 0 and 1");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category must be specified");
        }
    }

}

