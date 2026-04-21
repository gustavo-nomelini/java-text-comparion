package com.prodbygus.javatextcomparion.infrastructure.similarity;

import com.prodbygus.javatextcomparion.domain.service.SimilarityMetric;
import com.prodbygus.javatextcomparion.infrastructure.normalization.EnglishTextNormalizer;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Calculates Jaccard Similarity (set-based intersection over union).
 */
@Component
public class JaccardSimilarityCalculator implements SimilarityMetric {

    private final EnglishTextNormalizer normalizer;

    public JaccardSimilarityCalculator(EnglishTextNormalizer normalizer) {
        this.normalizer = normalizer;
    }

    @Override
    public double calculate(String text1, String text2) {
        if (text1 == null || text1.trim().isEmpty() || text2 == null || text2.trim().isEmpty()) {
            return 0.0;
        }

        // Normalize and tokenize
        String normalized1 = normalizer.normalize(text1);
        String normalized2 = normalizer.normalize(text2);

        String[] tokens1 = normalizer.tokenize(normalized1);
        String[] tokens2 = normalizer.tokenize(normalized2);

        if (tokens1.length == 0 || tokens2.length == 0) {
            return 0.0;
        }

        // Create sets
        Set<String> set1 = new HashSet<>();
        Set<String> set2 = new HashSet<>();

        for (String token : tokens1) {
            set1.add(token);
        }
        for (String token : tokens2) {
            set2.add(token);
        }

        // Calculate intersection and union
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        if (union.isEmpty()) {
            return 0.0;
        }

        return (double) intersection.size() / union.size();
    }

    @Override
    public String getName() {
        return "Jaccard Similarity";
    }
}

