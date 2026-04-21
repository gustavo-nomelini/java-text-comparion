package com.prodbygus.javatextcomparion.domain.service;

/**
 * Service interface for normalizing and preprocessing text.
 */
public interface TextNormalizer {

    /**
     * Normalize text by applying preprocessing steps.
     *
     * @param text the raw text to normalize
     * @return the normalized text
     */
    String normalize(String text);

    /**
     * Tokenize text into words.
     *
     * @param text the text to tokenize
     * @return array of tokens
     */
    String[] tokenize(String text);

    /**
     * Remove common stopwords from text.
     *
     * @param text the text to process
     * @return text with stopwords removed
     */
    String removeStopwords(String text);
}

