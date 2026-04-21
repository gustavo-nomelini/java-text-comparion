package com.prodbygus.javatextcomparion.infrastructure.similarity;

import com.prodbygus.javatextcomparion.domain.service.CorrelationIndexCalculator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Calculates the composite correlation index from individual metrics.
 * Formula: (cosineSimilarity * cosineWeight) + (jaccardSimilarity * jaccardWeight)
 */
@Component
public class DefaultCorrelationIndexCalculator implements CorrelationIndexCalculator {

    private final double cosineWeight;
    private final double jaccardWeight;

    public DefaultCorrelationIndexCalculator(
            @Value("${similarity.weights.cosine:0.7}") double cosineWeight,
            @Value("${similarity.weights.jaccard:0.3}") double jaccardWeight) {
        this.cosineWeight = cosineWeight;
        this.jaccardWeight = jaccardWeight;

        // Validate weights sum to 1.0
        double sum = cosineWeight + jaccardWeight;
        if (Math.abs(sum - 1.0) > 0.001) {
            throw new IllegalArgumentException("Metric weights must sum to 1.0, but got: " + sum);
        }
    }

    @Override
    public double calculate(double cosineSimilarity, double jaccardSimilarity) {
        // Validate inputs
        if (cosineSimilarity < 0 || cosineSimilarity > 1) {
            throw new IllegalArgumentException("Cosine similarity must be between 0 and 1");
        }
        if (jaccardSimilarity < 0 || jaccardSimilarity > 1) {
            throw new IllegalArgumentException("Jaccard similarity must be between 0 and 1");
        }

        // Calculate weighted average
        return (cosineSimilarity * cosineWeight) + (jaccardSimilarity * jaccardWeight);
    }

    @Override
    public List<Double> getWeights() {
        return Arrays.asList(cosineWeight, jaccardWeight);
    }
}

