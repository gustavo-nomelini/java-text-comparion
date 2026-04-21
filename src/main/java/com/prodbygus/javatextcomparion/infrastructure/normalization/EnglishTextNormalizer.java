package com.prodbygus.javatextcomparion.infrastructure.normalization;

import com.prodbygus.javatextcomparion.domain.service.TextNormalizer;
import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of TextNormalizer with basic preprocessing and English stopword removal.
 */
@Component
public class EnglishTextNormalizer implements TextNormalizer {

    private static final Set<String> ENGLISH_STOPWORDS = new HashSet<>(Arrays.asList(
            "a", "an", "and", "are", "as", "at", "be", "by", "for", "from", "has", "he",
            "in", "is", "it", "its", "of", "on", "or", "that", "the", "to", "was", "were",
            "will", "with", "i", "me", "my", "we", "you", "your", "which", "who", "what",
            "when", "where", "why", "how", "do", "does", "did", "doing", "done", "should",
            "could", "would", "may", "might", "must", "can", "this", "these", "those", "than",
            "just", "only", "no", "not", "so", "if", "but", "because", "before", "after",
            "between", "among", "during", "within", "without", "through", "about", "above",
            "below", "under", "over", "out", "into", "up", "down", "off", "all", "each",
            "every", "both", "few", "more", "most", "other", "some", "such", "any", "many"
    ));

    @Override
    public String normalize(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "";
        }

        // Remove accents
        text = removeAccents(text);

        // Convert to lowercase
        text = text.toLowerCase();

        // Remove special characters except spaces and hyphens
        text = text.replaceAll("[^a-z0-9\\s\\-]", " ");

        // Remove extra whitespace
        text = text.replaceAll("\\s+", " ").trim();

        // Remove stopwords
        text = removeStopwords(text);

        return text;
    }

    @Override
    public String[] tokenize(String text) {
        if (text == null || text.trim().isEmpty()) {
            return new String[0];
        }

        return text.toLowerCase()
                .split("[\\s\\-]+");
    }

    @Override
    public String removeStopwords(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "";
        }

        String[] tokens = text.toLowerCase().split("\\s+");
        StringBuilder result = new StringBuilder();

        for (String token : tokens) {
            if (!ENGLISH_STOPWORDS.contains(token.toLowerCase())) {
                if (result.length() > 0) {
                    result.append(" ");
                }
                result.append(token);
            }
        }

        return result.toString();
    }

    /**
     * Remove accents from text.
     *
     * @param text the text to process
     * @return text with accents removed
     */
    private String removeAccents(String text) {
        String nfd = Normalizer.normalize(text, Normalizer.Form.NFD);
        return nfd.replaceAll("\\p{M}", "");
    }
}

