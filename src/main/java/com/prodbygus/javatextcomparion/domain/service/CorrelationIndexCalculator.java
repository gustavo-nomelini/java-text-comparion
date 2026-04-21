package com.prodbygus.javatextcomparion.domain.service;

import java.util.List;

/**
 * Service for calculating the composite correlation index from individual metrics.
 */
public interface CorrelationIndexCalculator {

    /**
     * Calculate the correlation index from individual metric scores.
     *
     * @param cosineSimilarity cosine similarity score (0-1)
     * @param jaccardSimilarity jaccard similarity score (0-1)
     * @return composite correlation index (0-1)
     */
    double calculate(double cosineSimilarity, double jaccardSimilarity);

    /**
     * Get the weights used in calculation.
     *
     * @return list of weights
     */
    List<Double> getWeights();
}

