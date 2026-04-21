package com.prodbygus.javatextcomparion.domain.service;

/**
 * Service interface for calculating similarity metrics between texts.
 */
public interface SimilarityMetric {

    /**
     * Calculate similarity score between two texts.
     *
     * @param text1 the first text
     * @param text2 the second text
     * @return similarity score between 0 and 1
     */
    double calculate(String text1, String text2);

    /**
     * Get the name of this metric.
     *
     * @return metric name
     */
    String getName();
}

