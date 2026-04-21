package com.prodbygus.javatextcomparion.domain.model;

/**
 * Categories for matched segments between documents.
 */
public enum SegmentCategory {
    /**
     * High similarity match (similarity > 0.7)
     */
    HIGHLY_SIMILAR,

    /**
     * Medium similarity match (0.4 < similarity <= 0.7)
     */
    RELATED,

    /**
     * Low similarity match (similarity <= 0.4)
     */
    DIVERGENT
}

