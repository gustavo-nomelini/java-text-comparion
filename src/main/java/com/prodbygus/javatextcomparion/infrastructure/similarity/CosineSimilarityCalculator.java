package com.prodbygus.javatextcomparion.infrastructure.similarity;

import com.prodbygus.javatextcomparion.domain.service.SimilarityMetric;
import com.prodbygus.javatextcomparion.infrastructure.normalization.EnglishTextNormalizer;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Calculates Cosine Similarity using TF-IDF weighting.
 */
@Component
public class CosineSimilarityCalculator implements SimilarityMetric {

    private final EnglishTextNormalizer normalizer;

    public CosineSimilarityCalculator(EnglishTextNormalizer normalizer) {
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

        // Create term frequency vectors
        Map<String, Double> vector1 = calculateTfIdf(tokens1, tokens2);
        Map<String, Double> vector2 = calculateTfIdf(tokens2, tokens1);

        // Calculate cosine similarity
        return cosineSimilarity(vector1, vector2);
    }

    @Override
    public String getName() {
        return "Cosine Similarity (TF-IDF)";
    }

    /**
     * Calculate TF-IDF weights for tokens.
     *
     * @param tokens the tokens to weight
     * @param allTokens all tokens (for IDF calculation)
     * @return map of token to TF-IDF weight
     */
    private Map<String, Double> calculateTfIdf(String[] tokens, String[] allTokens) {
        Map<String, Double> tfidf = new HashMap<>();
        Set<String> uniqueTokens = new HashSet<>(Arrays.asList(allTokens));

        for (String token : tokens) {
            double tf = (double) countOccurrences(tokens, token) / tokens.length;
            double idf = Math.log((double) uniqueTokens.size() / (countOccurrences(allTokens, token) + 1));
            tfidf.put(token, tf * idf);
        }

        return tfidf;
    }

    /**
     * Count occurrences of a token in array.
     *
     * @param tokens the token array
     * @param token the token to count
     * @return count
     */
    private int countOccurrences(String[] tokens, String token) {
        int count = 0;
        for (String t : tokens) {
            if (t.equals(token)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Calculate cosine similarity between two vectors.
     *
     * @param vector1 first vector
     * @param vector2 second vector
     * @return cosine similarity
     */
    private double cosineSimilarity(Map<String, Double> vector1, Map<String, Double> vector2) {
        double dotProduct = 0.0;
        double magnitude1 = 0.0;
        double magnitude2 = 0.0;

        // Calculate dot product and magnitudes
        Set<String> allKeys = new HashSet<>(vector1.keySet());
        allKeys.addAll(vector2.keySet());

        for (String key : allKeys) {
            double val1 = vector1.getOrDefault(key, 0.0);
            double val2 = vector2.getOrDefault(key, 0.0);
            dotProduct += val1 * val2;
            magnitude1 += val1 * val1;
            magnitude2 += val2 * val2;
        }

        magnitude1 = Math.sqrt(magnitude1);
        magnitude2 = Math.sqrt(magnitude2);

        if (magnitude1 == 0 || magnitude2 == 0) {
            return 0.0;
        }

        return dotProduct / (magnitude1 * magnitude2);
    }
}

