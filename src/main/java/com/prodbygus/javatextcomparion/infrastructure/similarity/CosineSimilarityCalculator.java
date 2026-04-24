package com.prodbygus.javatextcomparion.infrastructure.similarity;

import com.prodbygus.javatextcomparion.domain.service.SimilarityMetric;
import com.prodbygus.javatextcomparion.domain.service.TextNormalizer;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Calculates Cosine Similarity using TF-IDF weighting.
 */
@Component
public class CosineSimilarityCalculator implements SimilarityMetric {

    private final TextNormalizer normalizer;

    public CosineSimilarityCalculator(TextNormalizer normalizer) {
        this.normalizer = normalizer;
    }

    @Override
    public double calculate(String text1, String text2) {
        if (text1 == null || text1.trim().isEmpty() || text2 == null || text2.trim().isEmpty()) {
            return 0.0;
        }

        // Normalize texts
        String normalized1 = normalizer.normalize(text1);
        String normalized2 = normalizer.normalize(text2);

        // Tokenize
        String[] tokens1 = normalizer.tokenize(normalized1);
        String[] tokens2 = normalizer.tokenize(normalized2);

        if (tokens1.length == 0 || tokens2.length == 0) {
            return 0.0;
        }

        return cosineSimilarityTfIdf(tokens1, tokens2);
    }

    @Override
    public String getName() {
        return "Cosine Similarity (TF-IDF)";
    }

    /**
     * Cosine similarity using TF-IDF weighting with a 2-document corpus (docA/docB).
     * <p>
     * IDF uses smoothing to avoid division by zero:
     * <pre>
     * idf(t) = log((N + 1) / (df(t) + 1)) + 1
     * </pre>
     * where N = 2 and df(t) is the number of documents containing term t.
     */
    private double cosineSimilarityTfIdf(String[] tokens1, String[] tokens2) {
        Map<String, Integer> tf1 = termFrequencies(tokens1);
        Map<String, Integer> tf2 = termFrequencies(tokens2);

        int len1 = tokens1.length;
        int len2 = tokens2.length;

        Set<String> vocabulary = new HashSet<>(tf1.keySet());
        vocabulary.addAll(tf2.keySet());

        if (vocabulary.isEmpty()) {
            return 0.0;
        }

        final double n = 2.0;
        double dot = 0.0;
        double mag1 = 0.0;
        double mag2 = 0.0;

        for (String term : vocabulary) {
            int c1 = tf1.getOrDefault(term, 0);
            int c2 = tf2.getOrDefault(term, 0);
            int df = (c1 > 0 ? 1 : 0) + (c2 > 0 ? 1 : 0);

            // Smoothing keeps IDF positive and stable for small corpora.
            double idf = Math.log((n + 1.0) / (df + 1.0)) + 1.0;

            double w1 = (c1 == 0) ? 0.0 : ((double) c1 / len1) * idf;
            double w2 = (c2 == 0) ? 0.0 : ((double) c2 / len2) * idf;

            dot += w1 * w2;
            mag1 += w1 * w1;
            mag2 += w2 * w2;
        }

        if (mag1 == 0.0 || mag2 == 0.0) {
            return 0.0;
        }

        return dot / (Math.sqrt(mag1) * Math.sqrt(mag2));
    }

    private Map<String, Integer> termFrequencies(String[] tokens) {
        Map<String, Integer> counts = new HashMap<>();
        for (String token : tokens) {
            if (token == null || token.isBlank()) {
                continue;
            }
            counts.merge(token, 1, Integer::sum);
        }
        return counts;
    }
}

